package backend.Instructions.load;

import backend.Instructions.mipsInstruction;

public class LI extends mipsInstruction {
    public Integer reg = null;
    public Integer num = null;
    public long longNum;

    public LI(Integer reg, Integer num) {
        this.reg = reg;
        this.num = num;
    }

    public LI(Integer reg, long m,boolean flag) {
        this.reg = reg;
        this.longNum = m;
    }

    public String getOutput() {
        if(num!=null){
            return "li $" + reg + ", " + num;
        }else{
            return "li $" + reg + ", " + longNum;
        }

    }
}
