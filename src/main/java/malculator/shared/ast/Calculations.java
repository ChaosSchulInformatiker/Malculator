package malculator.shared.ast;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class Calculations {

    public HashMap<Integer,Double> derivative(HashMap<Integer,Double> sheet) {
        if(sheet == null) return null;
        var derivativeSheet = new HashMap<Integer,Double>();
        for(int i : sheet.keySet()) {
            derivativeSheet.put(i-1,sheet.get(i)*i);
        }
        return derivativeSheet;
    }

    public HashMap<Integer, Double> times(@NotNull HashMap<Integer,Double> sheet1, @NotNull HashMap<Integer,Double> sheet2) {
        var result = new HashMap<Integer,Double>();
        for(int i: sheet1.keySet()) {
            for(int j : sheet2.keySet()) {
                double res = sheet1.get(i)*sheet2.get(j);
                if(result.containsKey(i+j)) {
                    result.put(i+j,result.get(i+j)+res);
                }else {
                    result.put(i+j,res);
                }
            }
        }
        return result;
    }

    public HashMap<Integer,Double> plus(@NotNull HashMap<Integer,Double> sheet1, @NotNull HashMap<Integer,Double> sheet2, boolean plus) {

        for(int i : sheet2.keySet()) {
            if(sheet1.containsKey(i)) {
                double res = sheet1.get(i)+(plus ? 1 : -1)*sheet2.get(i);
                sheet1.put(i,res);
            }else {
                sheet1.put(i,(plus ? 1 : -1)*sheet2.get(i));
            }
        }
        return sheet1;
    }

}
