/*
 * IdaJava version 0.3
 * Copyright (c)2007-2010 by Christian Blichmann
 *
 * SWIG interface file for plugin exports
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

#undef NO_OBSOLETE_FUNCS

typedef error_t (idaapi *funcset_t_cb)(void);
%{typedef error_t (idaapi *funcset_t_cb)(void);%}
struct funcset_t
{
  int qnty;
  extfun_t *f;
  funcset_t_cb startup;
  funcset_t_cb shutdown;
};
%ignore funcset_t;

typedef bool (idaapi *extlang_t_compile_cb)(const char *name, ea_t current_ea, const char *expr, char *OUTPUT, size_t errbufsize);
%{typedef bool (idaapi *extlang_t_compile_cb)(const char *name, ea_t current_ea, const char *expr, char *errbuf, size_t errbufsize);%}
typedef bool (idaapi *extlang_t_run_cb)(const char *name, int nargs, const idc_value_t args[], idc_value_t *result, char *OUTPUT, size_t errbufsize);
%{typedef bool (idaapi *extlang_t_run_cb)(const char *name, int nargs, const idc_value_t args[], idc_value_t *result, char *errbuf, size_t errbufsize);%}
typedef bool (idaapi *extlang_t_calcexpr_cb)(ea_t current_ea, const char *expr, idc_value_t *rv, char *OUTPUT, size_t errbufsize);
%{typedef bool (idaapi *extlang_t_calcexpr_cb)(ea_t current_ea, const char *expr, idc_value_t *rv, char *errbuf, size_t errbufsize);%}
typedef bool (idaapi *extlang_t_compile_file_cb)(const char *file, char *OUTPUT, size_t errbufsize);
%{typedef bool (idaapi *extlang_t_compile_file_cb)(const char *file, char *errbuf, size_t errbufsize);%}
struct extlang_t
{
  size_t size;
  uint32 flags;
  const char *name;
  extlang_t_compile_cb compile;
  extlang_t_run_cb run;
  extlang_t_calcexpr_cb calcexpr;
  extlang_t_compile_file_cb compile_file;
  const char *fileext;         
};
%ignore extlang_t;

typedef uval_t (idaapi *getname_cb)(const char *name);
%{typedef uval_t (idaapi *getname_cb)(const char *name);%}
idaman bool ida_export CompileLine(const char *line, char *errbuf, size_t errbufsize, getname_cb getname_func=NULL);
%ignore CompileLine;
idaman bool ida_export ExecuteLine(const char *line, const char *func, getname_cb getname_func, int argsnum, const idc_value_t args[], idc_value_t *result, char *errbuf, size_t errbufsize);
%ignore ExecuteLine;

%ignore idc_stacksize; // Not exported in ida.lib
%ignore idc_calldepth; // Not exported in ida.lib
%ignore expr_printf; // Not exported in ida.lib
%ignore expr_sprintf; // Not exported in ida.lib
%ignore expr_printfer; // Not exported in ida.lib
%ignore init_idc; // Not exported in ida.lib
%ignore term_idc; // Not exported in ida.lib
%ignore del_idc_userfuncs; // Not exported in ida.lib
%ignore del_idc_userdefs; // Not exported in ida.lib
%ignore find_builtin_idc_func; // Not exported in ida.lib
%ignore idc_lx; // Not exported in ida.lib
%ignore idc_vars; // Not exported in ida.lib
%ignore idc_resolver_ea; // Not exported in ida.lib
%ignore idc_resolve_label; // Not exported in ida.lib
%ignore extlangs; // Not exported in ida.lib

%include <expr.hpp>