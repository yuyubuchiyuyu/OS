package backend.Instructions.calculate;

import backend.Instructions.mipsInstruction;

public class SRA extends mipsInstruction {  // 算术右移 扩展符号位
    public Integer resReg = null;
    public Integer reg = null;
    public Integer num = null;

    public SRA(Integer resReg, Integer reg, Integer num) {
        if (resReg == null || reg == null ) {
            throw new RuntimeException("SRA reg cannot be null.");
        }
        this.resReg = resReg;
        this.reg = reg;
        this.num = num;
    }

    public String getOutput() {
        return "sra $" + resReg + ", $" + reg + ", " + num;
    }
}
