package xml.file.generator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CopyExcelSheet {

	public static void main(String[] args) {
		
		CopyExcelSheet ces = new CopyExcelSheet();
		
		String excelFilePath = "/Users/hakimsmac/Downloads/PECOS_Input.xlsx";
		String excelFilePath1 = "/Users/hakimsmac/Documents/PECOS_Input.xlsx";
		String copySheetName = "Sheet1";
		
		
		List<List<String>> selectedRowDataList = ces.getExcelData(excelFilePath, copySheetName,1, 9);
		
		ces.createExcelSheetWithData(excelFilePath1, selectedRowDataList);

	}
	
	public void updateColunValue(String excelFilePath1) {
		
		try {
			FileInputStream fsIp = new FileInputStream(excelFilePath1.trim());
			HSSFWorkbook wb = new HSSFWorkbook(fsIp);
			HSSFSheet workSheet = wb.getSheetAt(0);
			Cell cell1 = null;
			Cell cell2 = null;
			int fRowNum = workSheet.getFirstRowNum();
			int lRowNum = workSheet.getLastRowNum();
			for (int i = fRowNum+1 ; i < lRowNum+1; i++) {
				Random rand = new Random();
				int n = rand.nextInt(9999999) + 1000000;
				int m = rand.nextInt(9999999) + 1000000;
				cell1 = workSheet.getRow(i).getCell(0);
				cell2 = workSheet.getRow(i).getCell(1);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	/* Get the data in excel file. 
	 * Return: 2D String list contains specified row data.
	 * excelFilePath :  The exist file path need to copy.
	 * excelSheetName : Which sheet to copy.
	 * startRow : Start row number.
	 * endRow : End row number.
	 * */
	private List<List<String>> getExcelData(String excelFilePath, String excelSheetName, int startRow, int endRow)
	{
		DataFormatter formatter = new DataFormatter(Locale.US);
		List<String> rowDataList = new ArrayList();
		List<List<String>> ret = new ArrayList();
		if(excelFilePath!=null && !"".equals(excelFilePath.trim()) && excelSheetName!=null && !"".equals(excelSheetName.trim()))
		{
			try{
				/* Open the file input stream. */
				FileInputStream fis = new FileInputStream(excelFilePath.trim());
	
				/* Get workbook. */
				Workbook excelWookBook = new XSSFWorkbook(fis);
	
				/* Get sheet by name. */
				Sheet copySheet = excelWookBook.getSheet(excelSheetName);
				
				int fRowNum = copySheet.getFirstRowNum();
				int lRowNum = copySheet.getLastRowNum();
				
				/*  First row is excel file header, so read data from row next to it. */
				for (int k = 0; k < 100; k++) {
					for(int i=fRowNum+1; i<lRowNum+1; i++)
					{
						/* Only get desired row data. */
						if(i>=startRow && i<=endRow)
						{
							Row row = copySheet.getRow(i);
							
							int fCellNum = row.getFirstCellNum();
							int lCellNum = row.getLastCellNum();
							
							/* Loop in cells, add each cell value to the list.*/
							 rowDataList = new ArrayList<String>();
							for(int j = fCellNum; j < lCellNum; j++)
							{
								Cell cell = row.getCell(j);
								String cValue = formatter.formatCellValue(cell);
								rowDataList.add(cValue);
							}
							
						}
						
					}
					ret.add(rowDataList);
				}
				
				
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return ret;
	}
	
	
	/* Create a new excel sheet with data. 
	 * excelFilePath :  The exist excel file need to create new sheet.
	 * dataList : Contains all the data that need save to the new sheet.
	 * */
	private void createExcelSheetWithData(String excelFilePath, List<List<String>> dataList)
	{
		if(excelFilePath!=null && !"".equals(excelFilePath.trim()))
		{
			try
			{
				/* Open the exist file input stream. */
				FileInputStream fis = new FileInputStream(excelFilePath.trim());
	
				/* Get workbook. */
				Workbook wookBook = new XSSFWorkbook(fis);
				
				/* Declare the new excel sheet. */
				Sheet newSheet = null;
				
				/* Get workbook sheet iterator. */
				Iterator sheetIterator = ((Iterable<Cell>) wookBook).iterator();
				
				/* If has existing sheet. */
				if(sheetIterator.hasNext())
				{
					newSheet = wookBook.cloneSheet(0);
					
					/* Select this new cloned sheet. */
					newSheet.setSelected(true);
					
					/* Because this is a cloned sheet, should remove all the cloned rows in it. */
					Iterator<Row> rowIterator = newSheet.iterator();
					while(rowIterator.hasNext())
					{
						// Get the clone row.
						Row cloneRow = rowIterator.next();
						
						// Remove the clone row.
						newSheet.removeRow(cloneRow);
						
						/* Because after remove the clone row, the iterator changed, 
						 * so get it again, else java.util.ConcurrentModificationException will be thrown .*/
						rowIterator = newSheet.iterator();
					}
				}else
				{
					newSheet = wookBook.createSheet("New Sheet");
				}
	
				/* Create header row. */
//				Row headerRow = newSheet.createRow(0);
//				
//				headerRow.createCell(0).setCellValue("Name");
//				headerRow.createCell(1).setCellValue("Password");
//				headerRow.createCell(2).setCellValue("Email");
//				headerRow.createCell(3).setCellValue("Age");
//				headerRow.createCell(4).setCellValue("Department");
//				headerRow.createCell(5).setCellValue("Skill");
				
	
				/* Loop in the row data list, add each row data into the new sheet. */
				if(dataList!=null)
				{
					int size = dataList.size();
					for(int i=0;i<size;i++)
					{
						List<String> cellDataList = dataList.get(i);
						
						/* Create row to save the copied data. */
						Row row = newSheet.createRow(i+1);
						
						int columnNumber = cellDataList.size();
						
						for(int j=0;j<columnNumber;j++)
						{
							String cellValue = cellDataList.get(j);
							row.createCell(j).setCellValue(cellValue);
						}
					}
				}
				
				/* Write the new sheet data to excel file */
				FileOutputStream fOut = new FileOutputStream(excelFilePath);
				wookBook.write(fOut);
				fOut.close();
				
				System.out.println("New sheet added in excel file " + excelFilePath + " successfully. ");
			}catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

}
