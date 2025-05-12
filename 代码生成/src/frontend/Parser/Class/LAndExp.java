package frontend.Parser.Class;

import frontend.Parser.Parser;
import middle.Class.Instruction.BinaryOperator;
import middle.Class.Instruction.TransferInst;
import middle.Class.IrType.IrType;
import middle.Count;
import middle.Symbol.SymbolTable;
import middle.Value;

import java.util.ArrayList;

public class LAndExp {
    // LAndExp → EqExp LAndExp'
    // LAndExp' → '&&' EqExp LAndExp' | ε
    private EqExp eqExp = null;
    private LAndExpTemp lAndExpTemp = null;
    public ArrayList<EqExp> eqExps;

    public LAndExp(EqExp eqExp, LAndExpTemp lAndExpTemp, ArrayList<String> outputList) {
        this.eqExp = eqExp;
        this.lAndExpTemp = lAndExpTemp;
        if (Parser.printFlag) {
            outputList.add("<LAndExp>");
        }
        this.eqExps = new ArrayList<>();
        eqExps.add(eqExp);
        LAndExpTemp lAndTemp = this.lAndExpTemp;
        while(lAndTemp!=null){
            eqExps.add(lAndTemp.getEqTemp());
            lAndTemp = lAndTemp.getLAndExpTemp();
        }
    }


}
