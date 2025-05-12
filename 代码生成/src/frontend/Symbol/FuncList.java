package frontend.Symbol;

import frontend.Parser.Class.*;

import java.util.ArrayList;

public class FuncList {
    private static ArrayList<FuncDef> funcDefs;

    public FuncList() {
        this.funcDefs = new ArrayList<>();
    }

    public void add(FuncDef newFuncDef) {
        for (FuncDef funcDef : funcDefs) {
            if (funcDef.getName().equals(newFuncDef.getName())) {
                return;
            }
        }
        funcDefs.add(newFuncDef);
    }

    public static boolean matchNum(String ident, FuncRParams funcRParams) {
        for (Integer i = 0; i < funcDefs.size(); i++) {
            FuncDef funcDef = funcDefs.get(i);
            if (funcDef.getName().equals(ident)) {
                FuncFParams funcFParams = funcDef.getFuncParams();
                if (funcRParams == null && funcFParams == null) {
                    return true;
                } else if (funcRParams == null || funcFParams == null) {
                    return false;
                }

                if (funcRParams.getExps() == null && funcFParams.getFuncFParams() == null) {
                    return true;
                } else if (funcRParams.getExps() == null || funcFParams.getFuncFParams() == null) {
                    return false;
                }

                return funcRParams.getExps().size() == funcFParams.getFuncFParams().size();
            }
        }
        return false;
    }

    public static boolean matchType(SymbolTable symbolTable,String ident, FuncRParams funcRParams) {
        for (Integer i = 0; i < funcDefs.size(); i++) {
            FuncDef funcDef = funcDefs.get(i);
            if (funcDef.getName().equals(ident)) { // 找到该函数
                FuncFParams funcFParams = funcDef.getFuncParams(); // 获取形参表

                if (funcRParams != null && funcFParams != null) {
                    ArrayList<Exp> exps = funcRParams.getExps();
                    ArrayList<FuncFParam> funcFParamsTemp = funcFParams.getFuncFParams();
                    if (exps != null && funcFParamsTemp != null) {

                        for (Integer j = 0; j < exps.size(); j++) {
                            if (j < funcFParamsTemp.size()) {

                                if (exps.get(j) == null && funcFParamsTemp.get(j) == null) {
                                    continue;
                                } else if (exps.get(j) == null || funcFParamsTemp.get(j) == null) {
                                    return false;
                                } else {
                                    String expString = exps.get(j).getType();
                                    String funcString = funcFParamsTemp.get(j).getType(symbolTable,ident);

                                    if (expString.contains("Const") && expString.contains("Array")) {
                                        System.out.println(expString + " " + funcString);
                                        return false;
                                    }
                                    expString.replace("Const", "");
                                    if (expString.contains("Array") && !funcString.contains("Array")) {
                                        return false;
                                    } else if (!expString.contains("Array") && funcString.contains("Array")) {
                                        return false;
                                    } else if (expString.equals("CharArray") && funcString.equals("IntArray")) {
                                        return false;
                                    } else if (expString.equals("IntArray") && funcString.equals("CharArray")) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }
}
