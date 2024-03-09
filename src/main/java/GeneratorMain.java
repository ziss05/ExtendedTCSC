import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

public class GeneratorMain {
    public static void main(String[] args) {
        ExampleGenerator generator = new ExampleGenerator(20, 3, 40, "../../TCP.xsd", "src/main/resources/output");
        generator.generate();
        Integer[][] statistics= generator.getStatistics();

        XSSFWorkbook workbook = new XSSFWorkbook();
        String sheetName = "Statistics";
        XSSFSheet sheet = workbook.createSheet(sheetName);


        int rowNum = 0;
        if (statistics != null) {
            for (Object[] objects : statistics) {
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

        try {
            FileOutputStream outputStream = new FileOutputStream("src/main/resources/Statistics.xlsx");
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
