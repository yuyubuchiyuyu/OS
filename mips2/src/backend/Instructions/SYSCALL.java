package backend.Instructions;

public class SYSCALL extends mipsInstruction{
    public SYSCALL(){}
    public String getOutput(){
        return "syscall";
    }
}
