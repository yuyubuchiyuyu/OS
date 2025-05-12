package backend;

public class Optimize {
    public static boolean optimize = false;
    public static boolean optimize1 = true && optimize; // mips修改类型转换
    public static boolean optimize2 = true && optimize;  // llvm删除continue break return后的死代码(没用)
    public static boolean optimize3 = true && optimize; // llvm删去冗余比较语句
    public static boolean optimize4 = true && optimize; // mips删除没用到的函数(没用)
    public static boolean optimize5 = true && optimize; // mips删去原地跳转指令
    public static boolean optimize6 = true && optimize; // 乘法优化

    public static boolean optimize7 = true && optimize; // 寄存器分配
    public static boolean optimize8 = true && optimize; // 跳转指令简化
    public static boolean optimize9 = true && optimize; // compareInst 节约寄存器
    public static boolean optimize10 = true && optimize;    // 加入s寄存器
    public static boolean optimize11 = true && optimize;    // 加入0寄存器
    public static boolean optimize12 = true && optimize;    // 除法优化
}
