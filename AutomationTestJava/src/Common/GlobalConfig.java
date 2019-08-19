package Common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Models.TestCase;

import Models.CommonConfig;

public class GlobalConfig {
	// Single Instant Pattern
	private static GlobalConfig _Instant = null;

	public static GlobalConfig Instant() throws Exception {
		if (_Instant == null) {
			_Instant = new GlobalConfig();
			_Instant.Read();
		}
		return _Instant;

	}

	public CommonConfig Config = new CommonConfig();
	public ArrayList<TestCase> TestCases = new ArrayList<TestCase>();

	public Map<String, Map<String, Map<String, String>>> TestKeywords = new HashMap<String, Map<String, Map<String, String>>>();

	// GlobalConfig Properties and functions
	public void Read() throws Exception {
		// Read config file
		// HSSFWorkbook hssfwb;
		Workbook workbook = new XSSFWorkbook(Constant.PATH_CONFIG);

		// Get Sheet Common Config
		Sheet sheet = workbook.getSheet(Constant.SHEET_COMMONCONFIG);
		if (sheet == null) {
			workbook.close();
			throw new Exception("Sheet {Constant.SHEET_COMMONCONFIG} isn't exsited.");
		}
		
		
        //Read Common Config
        for (int row = 1; row <= sheet.getLastRowNum(); row++)
        {
            var key = sheet.getRow(row).getCell(0).getStringCellValue();
            var pro = CommonConfig.class.getDeclaredField(key);
            if(pro != null)
            {
                pro.set(Config, sheet.getRow(row).getCell(1).getStringCellValue());
            }
        }
        
		 

		// Get Sheet TestCase
		sheet = workbook.getSheet(Constant.SHEET_TESTCASE);
		if (sheet != null) {
			// Read TestCase
			for (int row = 1; row <= sheet.getLastRowNum(); row++) {
				if (sheet.getRow(row).getLastCellNum() != 7) {
					continue;
				}
				var testCase = new TestCase();
				testCase.Name = sheet.getRow(row).getCell(0).getStringCellValue();
				testCase.DataVerion = sheet.getRow(row).getCell(1).getStringCellValue();
				testCase.AssemblyName = sheet.getRow(row).getCell(2).getStringCellValue();
				testCase.Class = sheet.getRow(row).getCell(3).getStringCellValue();
				testCase.Method = sheet.getRow(row).getCell(4).getStringCellValue();
				testCase.Description = sheet.getRow(row).getCell(5).getStringCellValue();
				testCase.IsEnabled = sheet.getRow(row).getCell(6).getBooleanCellValue();
				TestCases.add(testCase);
			}
		}

		// Get Sheet Keyword
		sheet = workbook.getSheet(Constant.SHEET_KEYWORD);
		if (sheet != null) {
			// Read Keyword
			for (int row = 1; row <= sheet.getLastRowNum(); row++) {
				if (sheet.getRow(row).getLastCellNum() != 4) {
					continue;
				}
				var testName = sheet.getRow(row).getCell(0).getStringCellValue();
				var dataVersion = sheet.getRow(row).getCell(1).getStringCellValue();
				if (TestKeywords.containsKey(dataVersion) == false) {
					TestKeywords.put(dataVersion, new HashMap<String, Map<String, String>>());
				}
				if (TestKeywords.get(dataVersion).containsKey(testName) == false) {
					TestKeywords.get(dataVersion).put(testName, new HashMap<String, String>());
				}

				var key = sheet.getRow(row).getCell(2).getStringCellValue();
				var data = sheet.getRow(row).getCell(3).getStringCellValue();
				TestKeywords.get(dataVersion).get(testName).put(key, data);

			}
		}

		workbook.close();
	}
}
