package middle.Class;

import middle.Class.Instruction.AllocaInst;
import middle.Class.Instruction.LabelInst;
import middle.Value;

import java.util.ArrayList;

public class BasicBlock extends Value {

    private ArrayList<Value> instructions;

    public BasicBlock(ArrayList<Value> instructions) {
        this.instructions = instructions;
    }

    public String getOutput() {
        StringBuilder res = new StringBuilder();
        for (Value instruction : instructions) {
            if (instruction instanceof LabelInst) {
                res.append("\n\t" + instruction.getOutput() + "\n");
            } else {
                res.append("\t" + instruction.getOutput() + "\n");
            }
        }
        return res.toString();
    }

    public ArrayList<Value> getInstructions() {
        return instructions;
    }
}
