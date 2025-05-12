package backend.Instructions;

public class MOVE extends mipsInstruction {
    public Integer resReg = null;
    public Integer reg = null;

    public MOVE(Integer resReg, Integer reg) {
        this.resReg = resReg;
        this.reg = reg;
    }

    public String getOutput() {
        return "move $" + resReg + ", $" + reg;
    }
}
