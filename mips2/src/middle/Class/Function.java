package middle.Class;



import middle.Class.Instruction.ReturnInst;
import middle.Class.IrType.IrType;
import middle.Value;

import java.util.ArrayList;

public class Function extends Value {
    public IrType irType;
    public String name;
    public ArrayList<Argument> arguments = new ArrayList<>();
    public ArrayList<Value> instructions = new ArrayList<>();
    public ArrayList<BasicBlock> basicBlocks = new ArrayList<>();
    public boolean returnFlag;
    public Function(IrType irType, String name, ArrayList<Argument> arguments, ArrayList<Value> instructions, ArrayList<BasicBlock> basicBlocks,boolean returnFlag){
        this.irType = irType;
        this.name = name;
        this.arguments = arguments;
        this.instructions = instructions;
        this.basicBlocks = basicBlocks;
        this.returnFlag = returnFlag;
    }

    public Function(IrType irType, String name, ArrayList<Argument> arguments, ArrayList<BasicBlock> basicBlocks,boolean returnFlag){
        this.irType = irType;
        this.name = name;
        this.arguments = arguments;
        this.basicBlocks = basicBlocks;
        this.returnFlag = returnFlag;
    }

    public String getOutput(){
        StringBuilder res = new StringBuilder();
        res.append("define dso_local "+irType.getOutput()+" @" + name+"(");
        for(Integer i = 0;i<arguments.size();i++){
            res.append(arguments.get(i).getOutput());
            if(i!=arguments.size()-1){
                res.append(",");
            }
        }
        res.append(") #0 {\n");
        for(Value value:instructions){
            res.append("\t"+value.getOutput()+"\n");
        }
        for(BasicBlock block:basicBlocks){
            res.append(block.getOutput());
        }
        if(!returnFlag){
            ReturnInst returnInst = new ReturnInst(new IrType(IrType.TypeID.VoidTyID));
            res.append("\t"+returnInst.getOutput()+"\n");
        }
        res.append("}\n");
        return res.toString();
    }

}
