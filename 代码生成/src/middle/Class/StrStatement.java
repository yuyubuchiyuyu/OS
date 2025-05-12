package middle.Class;

public class StrStatement {
    private String name = null;
    private Integer size = null;
    private String text = null;
    public StrStatement(String name,Integer size,String text){
        this.name = name;
        this.size = size;
        this.text = text;
    }
    public String getOutput(){
        return name+" = private unnamed_addr constant ["+size+" x i8] c\""+text+"\",align 1";
    }
}
