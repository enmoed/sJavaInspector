import oop.ex6.parser.Parser;
import oop.ex6.trackers.FuncTracker;
import oop.ex6.trackers.VarTracker;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

    private static String[] royTests = {"/Users/roy/Documents/university/oop/sJavaInspector/supplied_material/tests/t_roy_1.sjava",
            "/Users/roy/Documents/university/oop/sJavaInspector/supplied_material/tests/t_roy_2.sjava",
            "/Users/roy/Documents/university/oop/sJavaInspector/supplied_material/tests/t_roy_3.sjava",
            "/Users/roy/Documents/university/oop/sJavaInspector/supplied_material/tests/t_roy_4.sjava",
    };

    public static List<String> getFilesStartingWith(String folderPath, String prefix) {
        List<String> filePaths = new ArrayList<String>();
        File folder = new File(folderPath);

        for (File file : folder.listFiles()) {
            if (file.isFile() && file.getName().startsWith(prefix)) {
                filePaths.add(file.getAbsolutePath());
            }
        }

        Collections.sort(filePaths, new Comparator<String>() {
            @Override
            public int compare(String file1, String file2) {
                String[] parts1 = file1.split("_");
                String[] parts2 = file2.split("_");
                int n1 = Integer.parseInt(parts1[parts1.length - 1].replace(".sjava",""));
                int n2 = Integer.parseInt(parts2[parts2.length - 1].replace(".sjava",""));
                return n1 - n2;
            }
        });
        return filePaths;
    }

    public static void main(String[] args) {
        for (var t : getFilesStartingWith("/Users/roy/Documents/university/oop/sJavaInspector/supplied_material/tests","t_roy_")) {
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
