//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.alibaba.excel.analysis.v07;

import com.alibaba.excel.analysis.BaseSaxAnalyser;
import com.alibaba.excel.analysis.v07.XlsxRowHandler;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.metadata.Sheet;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFReader.SheetIterator;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.WorkbookDocument;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.WorkbookDocument.Factory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class XlsxSaxAnalyser extends BaseSaxAnalyser {
    private XSSFReader xssfReader;
    private SharedStringsTable sharedStringsTable;
    private List<XlsxSaxAnalyser.SheetSource> sheetSourceList = new ArrayList();
    private boolean use1904WindowDate = false;

    public XlsxSaxAnalyser(AnalysisContext analysisContext) throws IOException, OpenXML4JException, XmlException {
        this.analysisContext = analysisContext;
        analysisContext.setCurrentRowNum(Integer.valueOf(0));
        this.xssfReader = new XSSFReader(OPCPackage.open(analysisContext.getInputStream()));
        this.sharedStringsTable = this.xssfReader.getSharedStringsTable();
        InputStream workbookXml = this.xssfReader.getWorkbookData();
        WorkbookDocument ctWorkbook = Factory.parse(workbookXml);
        CTWorkbook wb = ctWorkbook.getWorkbook();
        CTWorkbookPr prefix = wb.getWorkbookPr();
        if(prefix != null) {
            this.use1904WindowDate = prefix.getDate1904();
        }

        this.analysisContext.setUse1904WindowDate(this.use1904WindowDate);
        this.sheetSourceList = new ArrayList();
        SheetIterator ite = (SheetIterator)this.xssfReader.getSheetsData();

        while(ite.hasNext()) {
            InputStream inputStream = ite.next();
            String sheetName = ite.getSheetName();
            XlsxSaxAnalyser.SheetSource sheetSource = new XlsxSaxAnalyser.SheetSource(sheetName, inputStream);
            this.sheetSourceList.add(sheetSource);
        }

    }

    protected void execute() {
        Sheet sheetParam = this.analysisContext.getCurrentSheet();
        if(sheetParam != null && sheetParam.getSheetNo() > 0 && this.sheetSourceList.size() >= sheetParam.getSheetNo()) {
            InputStream var5 = ((XlsxSaxAnalyser.SheetSource)this.sheetSourceList.get(sheetParam.getSheetNo() - 1)).getInputStream();
            this.parseXmlSource(var5);
        } else {
            int i = 0;
            Iterator var3 = this.sheetSourceList.iterator();

            while(var3.hasNext()) {
                XlsxSaxAnalyser.SheetSource sheetSource = (XlsxSaxAnalyser.SheetSource)var3.next();
                ++i;
                this.analysisContext.setCurrentSheet(new Sheet(i));
                this.parseXmlSource(sheetSource.getInputStream());
            }
        }

    }

    private void parseXmlSource(InputStream inputStream) {
        InputSource sheetSource = new InputSource(inputStream);

        try {
            SAXParserFactory e = SAXParserFactory.newInstance();
            e.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            e.setFeature("http://xml.org/sax/features/external-general-entities", false);
            e.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            SAXParser saxParser = e.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            XlsxRowHandler handler = new XlsxRowHandler(this, this.sharedStringsTable, this.analysisContext);
            xmlReader.setContentHandler(handler);
            xmlReader.parse(sheetSource);
            inputStream.close();
        } catch (Exception var7) {
            var7.printStackTrace();
            throw new ExcelAnalysisException(var7);
        }
    }

    public List<Sheet> getSheets() {
        ArrayList sheets = new ArrayList();
        int i = 1;
        Iterator var3 = this.sheetSourceList.iterator();

        while(var3.hasNext()) {
            XlsxSaxAnalyser.SheetSource sheetSource = (XlsxSaxAnalyser.SheetSource)var3.next();
            Sheet sheet = new Sheet(i, 0);
            sheet.setSheetName(sheetSource.getSheetName());
            ++i;
            sheets.add(sheet);
        }

        return sheets;
    }

    class SheetSource {
        private String sheetName;
        private InputStream inputStream;

        public SheetSource(String sheetName, InputStream inputStream) {
            this.sheetName = sheetName;
            this.inputStream = inputStream;
        }

        public String getSheetName() {
            return this.sheetName;
        }

        public void setSheetName(String sheetName) {
            this.sheetName = sheetName;
        }

        public InputStream getInputStream() {
            return this.inputStream;
        }

        public void setInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }
    }
}
