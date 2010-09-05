/*
 * IDAJava version 0.3
 * Copyright (c)2007-2010 Christian Blichmann
 *
 * IDAJavaPlugin Class
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.blichmann.idajava.plugin;

import de.blichmann.idajava.api.IdaConsole;
import de.blichmann.idajava.api.plugin.IdaPlugin;
import de.blichmann.idajava.api.ui.EmbeddedFrameCreationException;
import de.blichmann.idajava.api.ui.IdaEmbeddedFrameFactory;
import de.blichmann.idajava.natives.IdaJava;
import de.blichmann.idajava.natives.IdaMenuItemListener;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import javax.swing.*;

/**
 * Default plugin that prepares the environment and calls all other Java
 * plugins.
 * @author Christian Blichmann
 * @since 0.2
 */
public class IdaJavaPlugin extends IdaPlugin {

	private String libPath;
	private ArrayList<IdaPlugin> plugins;
	private ArrayList<String> pluginMenuPaths;

	@Override
	public String getDisplayName() {
		// Display name set by native plugin
		return null;
	}

	@Override
	public String getHotkey() {
		// Hotkey set by native plugin
		return null;
	}

	private File[] listJarFiles(File path) {
		return path.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return !name.equalsIgnoreCase("idajava.jar") &&
						name.endsWith(".jar");
			}
		});
	}

	private List<String> listJarClassNames(final JarInputStream jis)
			throws IOException {
		final ArrayList<String> classNames = new ArrayList<String>();
		JarEntry loopEntry;
		loopEntry = jis.getNextJarEntry();
		while (loopEntry != null) {
			final JarEntry entry = loopEntry;
			loopEntry = jis.getNextJarEntry();
			// Skip directories
			if (entry.isDirectory())
				continue;
			// Skip non .class-files
			final String entryName = entry.getName(); 
			if (!entryName.endsWith(".class"))
				continue;

			classNames.add(entryName.substring(0, entryName.length() - 6)
					.replace('/', '.'));
		}
		return classNames;
	}
	
	@SuppressWarnings("unchecked")
	private <T> T newInstanceOfChildClass(final String className,
			final Class<?> superclass, final ClassLoader loader) {
		Class<?> klass;
		try {
			klass = Class.forName(className, true, loader);
			if (klass == null)
				return null;
		} catch (NoClassDefFoundError e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		}

		// Skip anything without a superclass, can happen if a JAR
		// contains only parts of a class hierarchy 
		Class<?> superklass = klass.getSuperclass();
		if (superklass == null || !superklass.equals(superclass))
			return null;

		try {
			return (T) klass.newInstance();
		} catch (InstantiationException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		}
	}
	
	private List<IdaPlugin> loadPluginsFromDirectory(File directory) {
		File[] jars = listJarFiles(directory);

		ArrayList<URL> urlList = new ArrayList<URL>();
		ArrayList<String> classNames = new ArrayList<String>();
		for (File path : jars) {
			FileInputStream fis;
			try {
				fis = new FileInputStream(path.getAbsolutePath());
			} catch (FileNotFoundException e) {
				System.err.println(e.getMessage());
				continue;
			}

			final List<String> jarClassNames;
			try {
				final JarInputStream jis = new JarInputStream(fis);
				jarClassNames = listJarClassNames(jis);
				if (jarClassNames.size() == 0)
					continue;
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}

			try {
				urlList.add(path.toURI().toURL());
			} catch (MalformedURLException e) { /* Eat*/ }
			classNames.addAll(jarClassNames);
			System.out.println("Loading " + path.getName() + "...");
		}

		final URL[] urls = (URL[]) urlList.toArray(new URL[urlList.size()]);
		final URLClassLoader cl = new URLClassLoader(urls,
				this.getClass().getClassLoader());
		final ArrayList<IdaPlugin> pluginList = new ArrayList<IdaPlugin>();
		for (final String className : classNames) {
			final IdaPlugin plugin = newInstanceOfChildClass(className,
					IdaPlugin.class, cl);
			if (plugin != null)
				pluginList.add(plugin);
		}
		return pluginList;
	}

	/**
	 * Constructs a new {@code IDAJavaPlugin} object. Tries to load the native
	 * IDAJava plugin library.
	 * @param libPath absolute filename of the plugin's shared library
	 */
	public IdaJavaPlugin(String libPath) {
		this.libPath = null;
		try {
			System.load(libPath);
			this.libPath = libPath;
		} catch (UnsatisfiedLinkError e) {
			System.err.println("Failed to bind to IDAJava plugin: " + e);
		}
		plugins = new ArrayList<IdaPlugin>();
		pluginMenuPaths = new ArrayList<String>();
	}

	private class IdaPluginMenuItemListener extends IdaMenuItemListener {
		private final int index;

		public IdaPluginMenuItemListener(int index) {
			super();
			this.index = index;
		}

		public int getIndex() {
			return index;
		}
	}
	
	private static final String MENU_PATH = "edit/plugins";

	/**
	 * Initializes the plugin.
	 * @return {@code PLUGIN_OK} to keep the plugin loaded, {@code PLUGIN_SKIP}
	 *     on error
	 */
	@Override
	public int initialize() {
		if (libPath == null)
			return PLUGIN_SKIP;

		IdaConsole.out.println("Loading Java plugins...");
		File pluginRoot = new File(libPath).getParentFile();
		List<IdaPlugin> loadedPlugins = loadPluginsFromDirectory(pluginRoot);
		pluginRoot = new File(pluginRoot.getParentFile(), "java");
		if (pluginRoot.isDirectory())
			loadedPlugins.addAll(loadPluginsFromDirectory(pluginRoot));

		for (IdaPlugin plugin : loadedPlugins) {
			String displayName;
			String hotkey;
			try {
				if (plugin.initialize() == PLUGIN_SKIP)
					continue;
				displayName = plugin.getDisplayName();
				if (displayName == null)
					displayName = plugin.getClass().getCanonicalName();
				hotkey = plugin.getHotkey();
			} catch (Exception e) {
				e.printStackTrace(IdaConsole.out);
				continue;
			}

			plugins.add(plugin);
			IdaJava.addMenuItem(MENU_PATH, displayName, hotkey, 1,
				new IdaPluginMenuItemListener(plugins.size() - 1) {
					@Override
					public void actionPerformed() {
						IdaConsole.out.println("actionPerformed()");
						IdaPlugin plugin = plugins.get(getIndex());
						try {
							plugin.run(0);
						} catch (Exception e) {
							e.printStackTrace(IdaConsole.out);
						}
					}
				});
			pluginMenuPaths.add(MENU_PATH + "/" + displayName.toLowerCase());
		}
		return PLUGIN_OK;
	}
	
	private void showAbout() {
		final Frame frame;
		try {
			frame = IdaEmbeddedFrameFactory.createFrame("IdaJava");
		} catch (EmbeddedFrameCreationException e) {
			e.printStackTrace(IdaConsole.out);
			return;
		}
        frame.setLayout(new BorderLayout());
        final JPanel backgroundPanel = new JPanel();
        backgroundPanel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        frame.add(backgroundPanel);

        final JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("About IdaJava"));
        backgroundPanel.add(panel, BorderLayout.CENTER);
        panel.setBounds(new Rectangle(8, 8, 400, 280));
        
        int yoff = 0;

        final JLabel appNameLabel = new JLabel("IdaJava Plugin for IDA Pro");
        panel.add(appNameLabel);
        yoff += 40;
        appNameLabel.setBounds(new Rectangle(24, yoff, 352, 20));

        final JLabel versionLabel = new JLabel("Version 0.3 (32-bit)");
        panel.add(versionLabel);
        yoff += 24;
        versionLabel.setBounds(new Rectangle(32, yoff, 344, 20));

        final JLabel copyrightLabel = new JLabel("Copyright (c)2007-2010 Christian Blichmann");
        panel.add(copyrightLabel);
        yoff += 20;
        copyrightLabel.setBounds(new Rectangle(32, yoff, 344, 20));

        final JLabel emailLabel = new JLabel("<feedback@blichmann.de>");
        panel.add(emailLabel);
        yoff += 16;
        emailLabel.setBounds(new Rectangle(32, yoff, 344, 20));

        final JLabel gplLabel0 = new JLabel("IdaJava comes with ABSOLUTELY NO WARRANTY. This is");
        panel.add(gplLabel0);
        yoff += 32;
        gplLabel0.setBounds(new Rectangle(32, yoff, 344, 20));
        final JLabel gplLabel1 = new JLabel("free software, and you are welcome to redistribute it under");
        panel.add(gplLabel1);
        yoff += 20;
        gplLabel1.setBounds(new Rectangle(32, yoff, 344, 20));
        final JLabel gplLabel2 = new JLabel("certain conditions; see included file LICENSE.txt for details.");
        panel.add(gplLabel2);
        yoff += 20;
        gplLabel2.setBounds(new Rectangle(32, yoff, 344, 20));

        backgroundPanel.setVisible(true);
	}

	@Override
	public void run(int arg) {
		if (arg >= 1000) {
			int index = arg - 1000;
			if (index < plugins.size()) {
				try {
					plugins.get(index).run(0);
				} catch(Exception e) {
					e.printStackTrace(IdaConsole.out);
				}
			}
			return;
		}

		if (arg == 0) {
			showAbout();
			return;
		}
		IdaConsole.out.println();
	}

	@Override
	public void terminate() {
		IdaConsole.out.println("Java in terminate() method");
		for (final IdaPlugin plugin : plugins) {
			try {
				plugin.terminate();
			} catch (Exception e) {
				e.printStackTrace(IdaConsole.out);
				continue;
			}
		}
		for (final String menuPath : pluginMenuPaths)
			IdaJava.del_menu_item(menuPath);
	}
}
