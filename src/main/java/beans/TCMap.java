package beans;

import com.github.kiprobinson.bigfraction.BigFraction;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class TCMap {

    private final String BASIC_SERVICE = "BS";
    private TreeSet<Quadruple> tcMap;

    public TCMap() {
        this.tcMap = new TreeSet<>();
    }

    public TCMap(TCMap tcMap) {
        this.tcMap = new TreeSet<>(tcMap.getTCMap());
    }

    public TCMap(String pathToExcelFile) {
        FileInputStream file = null;
        try {
            file = new FileInputStream(pathToExcelFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Excel file not found!", e);
        }

        Workbook workbook = null;
        try {
            workbook = new XSSFWorkbook(file);
        } catch (IOException e) {
            throw new RuntimeException("Error while trying to open workbook!", e);
        }
        Sheet sheet = workbook.getSheetAt(0);

        Map<Integer, List<String>> data = new HashMap<>();

        int i = 0;
        for (Row row : sheet) {
            data.put(i, new ArrayList<String>());
            for (Cell cell : row) {
                CellType cellType = cell.getCellType().equals(CellType.FORMULA) ? cell.getCachedFormulaResultType() : cell.getCellType();
                switch (cellType) {
                    case STRING:
                        data.get(i).add(cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {
                            throw new RuntimeException("Datatype date is not supported!");
                        } else {
                            data.get(i).add(cell.getNumericCellValue() + "");
                        }
                        break;
                    case BOOLEAN:
                        throw new RuntimeException("Datatype boolean is not supported!");
                    default:
                        break;
                }
            }
            i++;
        }

        try {
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        TCMap map = new TCMap();
        for (int j = 0; j < sheet.getLastRowNum(); j++) {
            List<String> row = data.get(j);
            if (row.size() != 4) {
                throw new RuntimeException("Only four values for each row allowed!");
            }
            map.add(new Quadruple(BASIC_SERVICE, new BigFraction(row.get(0)), row.get(1).equals("null") ? null : new BigFraction(row.get(1)), new BigFraction(row.get(2)), row.get(3).equals("null") ? null : new BigFraction(row.get(3))));
        }
        this.tcMap = map.tcMap;
    }


    public Set<Quadruple> getTCMap() {
        return new TreeSet<>(this.tcMap);
    }

    public boolean add(Quadruple quadruple) {
        return tcMap.add(quadruple);
    }

    public boolean addAll(Set<Quadruple> quadruples) {
        return tcMap.addAll(quadruples);
    }

    public BigFraction getMin() {
        return this.tcMap.first().getD1();
    }

    public BigFraction getMax() {
        return this.tcMap.last().getD2();
    }

    public Quadruple getFirst() {
        return this.tcMap.first();
    }

    public Quadruple getLast() {
        return this.tcMap.last();
    }

    public BigFraction[] getRange() {
        BigFraction[] range = new BigFraction[2];
        range[0] = getMin();
        range[1] = getMax();
        return range;
    }

    public boolean isFunctional() {
        ArrayList<Quadruple> list = new ArrayList<>(this.getTCMap());
        int counter = 1;

        Iterator<Quadruple> iterator = this.tcMap.iterator();
        while (iterator.hasNext()) {
            if (counter == list.size()) {
                break;
            }
            Quadruple listq = list.get(counter);
            Quadruple tcmapq = iterator.next();
            if (listq.getD1().compareTo(tcmapq.getD1()) <= 0
                    && tcmapq.getD1().compareTo(listq.getD2()) < 0) {
                return false;
            }
            counter++;
        }
        return true;
    }

    public Set<BigFraction> getPointSet() {
        HashSet<BigFraction> points = new HashSet<>();

        for (Quadruple q : this.tcMap) {
            points.add(q.getD1());
            points.add(q.getD2());
        }

        return points;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("  TimeCostMap\n---------------\n");
        for (Quadruple q : this.tcMap) {
            sb.append(q.toString());
            sb.append("\n----------------------------------\n");
        }

        return sb.toString();
    }

    public String toStringExtended() {
        StringBuilder sb = new StringBuilder("  TimeCostMap\n---------------\n");
        for (Quadruple q : this.tcMap) {
            sb.append(q.toStringWithAncestors());
            sb.append("\n----------------------------------\n");
        }

        return sb.toString();
    }

    public int size() {
        return this.tcMap.size();
    }

    public boolean isEmpty() {
        return this.tcMap.isEmpty();
    }

    public boolean containsQuadruple(Quadruple quadruple) {
        return this.tcMap.contains(quadruple);
    }

    public Quadruple removeFirst() {
        return this.tcMap.pollFirst();
    }

    public Quadruple removeLast() {
        return this.tcMap.pollLast();
    }

    public void clear() {
        this.tcMap.clear();
    }

    public TCMap clone() {
        return (TCMap) this.tcMap.clone();
    }


    public void writeToNewExcelFile(String pathToFile, String sheetname) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetname);
        Object[][] field = new Object[this.tcMap.size()][4];
        int i = 0;
        for (Quadruple quadruple : this.tcMap) {
            field[i][0] = quadruple.getD1();
            field[i][1] = quadruple.getC1();
            field[i][2] = quadruple.getD2();
            field[i][3] = quadruple.getC2();
            i++;
        }
        int rowNum = 0;
        for (Object[] objects : field) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            for (Object o : objects) {
                Cell cell = row.createCell(colNum++);
                if (o == null) {
                    cell.setCellValue("null");
                } else if (o instanceof Double) {
                    cell.setCellValue((Double) o);
                } else {
                    cell.setCellValue(o.toString());
                }
            }
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(pathToFile);
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean normalize() {
        if (!this.isFunctional()) {
            return false;
        }

        TCMap simple = new TCMap();
        Quadruple current = this.tcMap.first();
        Quadruple next = null;
        boolean first = true;
        boolean wasCurAdded = true;
        Iterator iterator = this.tcMap.iterator();

        while (iterator.hasNext()) {
            if (first) {
                current = (Quadruple) iterator.next();
                first = false;
                if (!iterator.hasNext()) {
                    return true;
                }
            }
            next = (Quadruple) iterator.next();
            if (current.getC1() != null) {
                if (current.joinable(next)) {
                    //provenance part
                    LinkedHashMap<Quadruple, ValidTuple> curancestors = current.getAncestors();
                    if (wasCurAdded) {
                        for (Quadruple q : curancestors.keySet()) {
                            curancestors.put(q, new ValidTuple(current.getD1(), current.getD2()));
                        }
                    }
                    LinkedHashMap<Quadruple, ValidTuple> nextancestors = next.getAncestors();
                    for (Quadruple q : nextancestors.keySet()) {
                        if (curancestors.containsKey(q)) {
                            curancestors.put(q, new ValidTuple(q.getD1(), next.getD2()));
                        } else curancestors.put(q, new ValidTuple(next.getD1(), next.getD2()));
                    }

                    //order important: handles duplicates
                    LinkedHashMap<Quadruple, ValidTuple> prov = new LinkedHashMap<>();
                    prov.putAll(nextancestors);
                    prov.putAll(curancestors);

                    current = new Quadruple(current.getName() == next.getName() ? current.getName() : "NORM", current.getD1(), current.getC1(), next.getD2(), next.getC2(), prov);
                    wasCurAdded = false;
                } else {
                    wasCurAdded = simple.add(current);
                    current = next;
                }
            } else current = next;
        }

        if (!wasCurAdded) {
            simple.add(current);
        } else if (next != null && next.getC1() != null) {
            simple.add(next);
        }
        this.tcMap = simple.tcMap;
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TCMap that = (TCMap) o;
        return this.tcMap.equals(that.tcMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tcMap);
    }

    public LinkedHashMap<Quadruple, ArrayList<ArrayList<ValidQuadruple>>> calcSetProvenance(Set<Quadruple> set){
        LinkedHashMap<Quadruple,ArrayList<ArrayList<ValidQuadruple>>> prov = new LinkedHashMap<>();
        for (Quadruple q: set) {
            ArrayList<ArrayList<ValidQuadruple>> tempProv=calcProvenance(q,null);
            prov.put(q,tempProv);
        }
        return prov;
    }
    public LinkedHashMap<Quadruple, ArrayList<ArrayList<ValidQuadruple>>> calcTreeProvenance(){
        return calcSetProvenance(this.tcMap);
    }


    public ArrayList<ArrayList<ValidQuadruple>> calcProvenance(Quadruple q, ValidTuple validPeriod) {
        ArrayList<ArrayList<ValidQuadruple>> prov = new ArrayList<>();
        ArrayList<ValidQuadruple> partList = new ArrayList<>();
        ValidTuple valid = validPeriod;
        if (valid == null) {
            valid = new ValidTuple(q.getD1(), q.getD2());
        }
        if (q == null || q.getAncestors() == null) {
            prov = null;
        } else calcProv(prov, partList, q, valid);

        if (!partList.isEmpty()) {
            prov.add(partList);
        }
        return prov;
    }

    public void calcProv(ArrayList<ArrayList<ValidQuadruple>> prov, ArrayList<ValidQuadruple> partList, Quadruple q, ValidTuple validPeriod) {
        if (q == null || q.getAncestors() == null) {
            partList=null;
            prov = null;
        }
        else if (q.getAncestors().isEmpty()) {
            BigFraction c1 = q.getC1().add(q.getGradient().multiply(validPeriod.getLow().subtract(q.getD1())));
            BigFraction c2 = q.getC1().add(q.getGradient().multiply(validPeriod.getUp().subtract(q.getD1())));
            partList.add(new ValidQuadruple(q.getName(), validPeriod.getLow(), c1, validPeriod.getUp(), c2));

        } else if (q.getAncestors().size() == 1) {
           /* ValidTuple ancestor = (ValidTuple) q.getAncestors().values().toArray()[0];
            if (q.getD1().compareTo(ancestor.getLow()) == 0 && q.getD2().compareTo(ancestor.getUp()) == 0) {*/
                calcProv(prov, partList, (Quadruple) q.getAncestors().keySet().toArray()[0], validPeriod);
//            } else calcProv(prov, partList, (Quadruple) q.getAncestors().keySet().toArray()[0], ancestor);
        } else if (q.getAncestors().size() > 1) {
            boolean samePeriod = true;
            BigFraction qd1 = q.getD1();
            BigFraction qd2 = q.getD2();
            BigFraction d1 = new BigFraction(0);

            for (ValidTuple t : q.getAncestors().values()) {
                if (qd1.compareTo(t.getLow()) != 0 || qd2.compareTo(t.getUp()) != 0) {
                    samePeriod = false;
                }
                d1=d1.add(t.getLow());
            }

            if (q.getAncestors().size() == 2 && (samePeriod || qd1.compareTo(d1) == 0)) {
                ArrayList<ArrayList<ValidQuadruple>> provTempL = new ArrayList<>();
                ArrayList<ValidQuadruple> partListTempL = new ArrayList<>();
                ArrayList<ArrayList<ValidQuadruple>> provTempR = new ArrayList<>();
                ArrayList<ValidQuadruple> partListTempR = new ArrayList<>();

                if (samePeriod) {


                    Object[] quadruples = q.getAncestors().keySet().toArray();
                    Quadruple left = (Quadruple) quadruples[0];
                    Quadruple right = (Quadruple) quadruples[1];

                    calcProv(provTempL, partListTempL, left, validPeriod);
                    calcProv(provTempR, partListTempR, right, validPeriod);


                } else {

                    LinkedHashMap<Quadruple, ValidTuple> children = q.getAncestors();
                    Object[] array = children.keySet().toArray();

                    Quadruple child1 = (Quadruple) array[0];
                    ValidTuple v1 = children.get(child1);
                    Quadruple child2 = (Quadruple) array[1];
                    ValidTuple v2 = children.get(child2);

                    if (v1.getLow() == v1.getUp()) {
                        calcProv(provTempL, partListTempL, child1, v1);
                        calcProv(provTempR, partListTempR, child2,
                                new ValidTuple(validPeriod.getLow().subtract(v1.getLow()), validPeriod.getUp().subtract(v1.getUp())));
                    } else {
                        calcProv(provTempL, partListTempL, child2, v2);
                        calcProv(provTempR, partListTempR, child1,
                                new ValidTuple(validPeriod.getLow().subtract(v2.getLow()), validPeriod.getUp().subtract(v2.getUp())));
                    }
                }

                if (provTempL.isEmpty()) {
                    if (provTempR.isEmpty()) {
                        partList.addAll(partListTempL);
                        partList.addAll(partListTempR);
                    } else { //partListTempL &&& provTempR
                        for (ArrayList<ValidQuadruple> list : provTempR) {
                            list.addAll(partListTempL);
                        }
                        prov.addAll(provTempR);
                    }
                } else {
                    if (provTempR.isEmpty()) {
                        //partListTempR &&& provTempL
                        for (ArrayList<ValidQuadruple> list : provTempL) {
                            list.addAll(partListTempR);
                        }
                        prov.addAll(provTempL);
                    } else {
                        for (ArrayList<ValidQuadruple> listL : provTempL) {
                            for (ArrayList<ValidQuadruple> listR : provTempR) {
                                ArrayList<ValidQuadruple> temp = new ArrayList<>(listL);
                                temp.addAll(listR);
                                prov.add(temp);
                            }
                        }
                    }
                }

            } else {
                //norm
                if ((q.getD1().compareTo(validPeriod.getLow()) == 0 && validPeriod.getUp().compareTo(q.getD2()) == 0)) {
                    for (Quadruple quadruple : q.getAncestors().keySet()) {
                        ArrayList<ArrayList<ValidQuadruple>> provTemp = new ArrayList<>();
                        ArrayList<ValidQuadruple> partListTemp = new ArrayList<>();
                        calcProv(provTemp, partListTemp, quadruple, q.getAncestors().get(quadruple));
                        prov.addAll(provTemp);
                        prov.add(partListTemp);
                    }
                } else {
                    for (Quadruple quadruple : q.getAncestors().keySet()) {
                        ValidTuple childValid = q.getAncestors().get(quadruple);
                        ArrayList<ArrayList<ValidQuadruple>> provTemp = new ArrayList<>();
                        ArrayList<ValidQuadruple> partListTemp = new ArrayList<>();

                        if (childValid.getLow().compareTo(validPeriod.getLow()) == 0 && validPeriod.getUp().compareTo(childValid.getUp()) == 0){
                            calcProv(provTemp, partListTemp, quadruple, childValid);
                        }
                        else if (childValid.getLow().compareTo(validPeriod.getLow()) < 0 &&
                                validPeriod.getUp().compareTo(childValid.getUp()) < 0){
                            calcProv(provTemp, partListTemp, quadruple, validPeriod);
                        }
                        else if (childValid.getLow().compareTo(validPeriod.getLow()) > 0 &&
                                validPeriod.getUp().compareTo(childValid.getUp()) > 0){
                            calcProv(provTemp, partListTemp, quadruple, childValid);
                        }
                        else if (childValid.getLow().compareTo(validPeriod.getLow()) > 0 &&
                                childValid.getLow().compareTo(validPeriod.getUp())<0 &&
                                validPeriod.getUp().compareTo(childValid.getUp()) < 0){

                            calcProv(provTemp, partListTemp, quadruple, new ValidTuple(childValid.getLow(),validPeriod.getUp()));

                        }
                        else if (childValid.getLow().compareTo(validPeriod.getLow()) < 0 &&
                                validPeriod.getLow().compareTo(childValid.getUp())<0 &&
                                validPeriod.getUp().compareTo(childValid.getUp()) > 0){

                            calcProv(provTemp, partListTemp, quadruple, new ValidTuple(validPeriod.getLow(),childValid.getUp()));

                        }

                        prov.addAll(provTemp);
                        prov.add(partListTemp);
                    }
                }
            }
        }
    }
}
