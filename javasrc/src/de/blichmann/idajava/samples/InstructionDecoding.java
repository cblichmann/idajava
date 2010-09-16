import de.blichmann.idajava.api.IdaConsole;
import de.blichmann.idajava.natives.IdaJava;
import de.blichmann.idajava.natives.IdaJavaConstants;
import de.blichmann.idajava.natives.RegNo;
import de.blichmann.idajava.natives.insn_t;
import de.blichmann.idajava.natives.op_t;
import de.blichmann.idajava.natives.optype_t;


public class IDATest extends de.blichmann.idajava.api.plugin.IdaPlugin {

	@Override
	public String getDisplayName() {
		return "Print the internal representation of an instruction at the cursor";
	}

	@Override
	public String getHotkey() {
		return "i";
	}
	
	@Override
	public void run(int arg) {
		long ea = IdaJava.get_screen_ea();
		
		IdaJava.decode_insn(ea);
		insn_t cmd = IdaJava.getCmd();
		
		IdaConsole.out.print(Long.toHexString(ea) + ": ");
		
		if ((cmd.getAuxpref() & IdaJavaConstants.aux_lock) != 0) {
			IdaConsole.out.print("lock ");
		}
		
		if ((cmd.getAuxpref() & IdaJavaConstants.aux_rep) != 0) {
			IdaConsole.out.print("rep ");
		} else if ((cmd.getAuxpref() & IdaJavaConstants.aux_repne) != 0) {
			IdaConsole.out.print("repne ");
		}
		
		IdaConsole.out.print(cmd.get_canon_mnem() + " ");
				
		op_t op;
		
		for (int i = 0; i<6 ; i++) {
			op = cmd.getOperand(i);
			
			if (op.getType() == optype_t.o_void) {
				break;
			} else {				
				switch (op.getType()) {
					case o_reg:
						IdaConsole.out.print(regToString(RegNo.swigToEnum(op.getReg()), (int)op.getDtyp()) + " ");
						break;
					case o_mem:
					case o_near:
					case o_far:
						IdaConsole.out.print("[" + op.getAddr() + "]'" + (int)Math.pow(2, (int)op.getDtyp()) + " ");
						break;
					case o_imm:
						IdaConsole.out.print(op.getAddr() + " ");
					case o_phrase:
					case o_displ:
						String tmp = "[";
						if (op.hasSIB()) {
							tmp = tmp + regToString(RegNo.swigToEnum(IdaJava.sib_base(op)),  2);
							if (RegNo.swigToEnum(IdaJava.sib_index(op)) != RegNo.R_sp) {
								tmp = tmp + "+" + regToString(RegNo.swigToEnum(IdaJava.sib_index(op)),  2);
								if (IdaJava.sib_scale(op) != 0) {
									tmp = tmp + "*" + (int)Math.pow(2, IdaJava.sib_scale(op));
								}
							}
						} else {
							tmp = tmp + regToString(RegNo.swigToEnum(op.getReg()),  2);	
						}
						if (op.getAddr() != 0) {
							tmp = tmp + "+" + op.getAddr();
						}
						tmp = tmp + "]'" + (int)Math.pow(2, (int)op.getDtyp()) + " ";
						IdaConsole.out.print(tmp);
						break;
					default:
						IdaConsole.out.print(RegNo.swigToEnum(op.getReg()).toString() + "(" + (int)op.getDtyp() + ")" + " ");
				}
				
				
			}
		}
		
		IdaConsole.out.print("\n");
		
		
		
	}
	
	public String regToString (RegNo r, int t) {
		String base = r.toString().substring(2);
		switch(t) {
		case 0:
		case 1:
			return base;
		case 2:
			return "e"+base;
		}
		
		return null;
	}

}
