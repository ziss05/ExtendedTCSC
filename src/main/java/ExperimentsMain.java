import beans.Node;
import beans.TCMap;
import beans.TimeoutException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xml.sax.SAXException;

import java.io.*;

public class ExperimentsMain {
    static final int NUMTESTRUNS=1;
    static final long TIMEOUT_NANOS=0l;//300000000000l;

    public static void main(String[] args) {
//     Add if you want to analyse with tools such as VisualVM
        /*try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        StringBuilder pathData = new StringBuilder("src/main/resources/input");

        File file = new File(pathData.toString());
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });

        String pathToExampleInputs = pathData.toString();

        XSSFWorkbook workbook = new XSSFWorkbook();
        String sheetName = "SpeedTestResults";
        Object[][] neededFields = null;
        XSSFSheet sheet = null;

        String[] subDirectories = null;

        for (int i = 0; i < directories.length; i++) {
            File examples = new File(pathData.append("/" + directories[i]).toString());
            subDirectories = examples.list(new FilenameFilter() {
                @Override
                public boolean accept(File current, String name) {
                    return new File(current, name).isFile();
                }
            });

            pathData = new StringBuilder(pathToExampleInputs);

            sheet = workbook.createSheet(sheetName + directories[i]);
            String pathToExampleInputsClasses = pathData.append("/" + directories[i]).toString();
            neededFields = new Object[subDirectories.length][NUMTESTRUNS+ NUMTESTRUNS+ 1]; //Name + Time each + TCMapSize each

            System.out.println(directories[i] + " started.");

            for (int j = 0; j < subDirectories.length; j++) {
                neededFields[j][0] = subDirectories[j];
                XMLImporter importer = null;
                try {
                    importer = new XMLImporter(new File("src/main/resources/TCP.xsd"), new File(pathData.append("/" + subDirectories[j]).toString()));
                    importer.importXMLData();
                } catch (SAXException e) {
                    e.printStackTrace();
                    System.out.println("Fail!");
                }

                Node root = importer.getTree();

                System.out.println(subDirectories[j] + " started.");

                long elapsedTime = 0;
                TCMap map=null;

                for (int k = 0; k < NUMTESTRUNS; k++) {
                    long startTime = System.nanoTime();//currentTimeMillis();
                        Node.setStartTime(startTime);
                        Node.setBenchmark(TIMEOUT_NANOS);
                    try {
                        map=root.calculateTCMap();
                    } catch (TimeoutException e) {
                        System.out.println("Error: " + e.getMessage() + " " + subDirectories[j] + " was stopped." );
                    }catch (NullPointerException ex){
                        System.out.println("Error NullPointer: " + ex.getMessage() + " " + subDirectories[j] + " was stopped." );
                    }
                    elapsedTime = 0;

/*For top-down procedure experiment*/
/*                    if (map != null) {
                        startTime = System.nanoTime();
                        map.calcTreeProvenance();
                    }*/

                    long stopTime = System.nanoTime();//currentTimeMillis();
                    elapsedTime = stopTime - startTime;
                    neededFields[j][1 + k*2] = elapsedTime;
                    if (map != null) {
                        neededFields[j][2 + k*2] = map.size();
//                        System.out.println(map);
//                        System.out.println(map.calcTreeProvenance());
//                        map.writeToNewExcelFile("src/main/resources/output/"+directories[i]+"_"+subDirectories[j]+"_TCMap.xlsx",directories[i]+"_"+subDirectories[j]+"_TCMap");
                        }
                    else {
                        neededFields[j][2 + k*2] = null;
                    }
                }


                System.out.println(subDirectories[j] + " completed.");
                pathData = new StringBuilder(pathToExampleInputsClasses);
            }
            pathData = new StringBuilder(pathToExampleInputs);

            int rowNum = 0;
            if (neededFields != null) {
                for (Object[] objects : neededFields) {
                    Row row = sheet.createRow(rowNum++);
                    int colNum = 0;
                    for (Object o : objects) {
                        Cell cell = row.createCell(colNum++);
                        if (o == null) {
                            cell.setCellValue("null");
                        } else if (o instanceof Long) {
                            cell.setCellValue((Long) o);
                        } else {
                            cell.setCellValue(o.toString());
                        }
                    }
                }
            }
            System.out.println(directories[i] + " completed.");
        }

        try {
            FileOutputStream outputStream = new FileOutputStream( "src/main/resources/output/SpeedTestResults.xlsx");
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

