import oop.ex6.parser.Parser;
import oop.ex6.trackers.FuncTracker;
import oop.ex6.trackers.VarTracker;

public class testRoy {
    private static String[] tests = {"/Users/roy/Documents/university/oop/sJavaInspector/supplied_material/tests/test003.sjava",
            "/Users/roy/Documents/university/oop/sJavaInspector/supplied_material/tests/test007.sjava",
            "/Users/roy/Documents/university/oop/sJavaInspector/supplied_material/tests/test011.sjava",
            "/Users/roy/Documents/university/oop/sJavaInspector/supplied_material/tests/test017.sjava",
            "/Users/roy/Documents/university/oop/sJavaInspector/supplied_material/tests/test052.sjava",
            "/Users/roy/Documents/university/oop/sJavaInspector/supplied_material/tests/test058.sjava",
            "/Users/roy/Documents/university/oop/sJavaInspector/supplied_material/tests/test061.sjava",
            "/Users/roy/Documents/university/oop/sJavaInspector/supplied_material/tests/test067.sjava",
            "/Users/roy/Documents/university/oop/sJavaInspector/supplied_material/tests/test106.sjava",
            "/Users/roy/Documents/university/oop/sJavaInspector/supplied_material/tests/test115.sjava",
            "/Users/roy/Documents/university/oop/sJavaInspector/supplied_material/tests/test204.sjava",
            "/Users/roy/Documents/university/oop/sJavaInspector/supplied_material/tests/test257.sjava",
            "/Users/roy/Documents/university/oop/sJavaInspector/supplied_material/tests/test261.sjava",
            "/Users/roy/Documents/university/oop/sJavaInspector/supplied_material/tests/test274.sjava",
            "/Users/roy/Documents/university/oop/sJavaInspector/supplied_material/tests/test407.sjava",
            "/Users/roy/Documents/university/oop/sJavaInspector/supplied_material/tests/test420.sjava",
            "/Users/roy/Documents/university/oop/sJavaInspector/supplied_material/tests/test427.sjava",
            "/Users/roy/Documents/university/oop/sJavaInspector/supplied_material/tests/test452.sjava",
            "/Users/roy/Documents/university/oop/sJavaInspector/supplied_material/tests/test474.sjava",
            "/Users/roy/Documents/university/oop/sJavaInspector/supplied_material/tests/test501.sjava",
    };

    public static void main(String[] args) {
        for (var t : tests) {
            try {
                VarTracker.reset();
                FuncTracker.reset();
                Parser.run(t);
                System.out.println(String.format("%s --- 0", t));
            } catch (Exception e) {
                System.out.println(String.format("%s --- 1 -- %s", t, e));
            }
        }
    }
}
