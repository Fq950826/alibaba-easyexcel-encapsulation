//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.alibaba.excel.analysis.v03;

import com.alibaba.excel.analysis.BaseSaxAnalyser;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.OneRowAnalysisFinishEvent;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.metadata.Sheet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.eventusermodel.FormatTrackingHSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFRequest;
import org.apache.poi.hssf.eventusermodel.MissingRecordAwareHSSFListener;
import org.apache.poi.hssf.eventusermodel.EventWorkbookBuilder.SheetRecordCollectingListener;
import org.apache.poi.hssf.eventusermodel.dummyrecord.LastCellOfRowDummyRecord;
import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingCellDummyRecord;
import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.BlankRecord;
import org.apache.poi.hssf.record.BoolErrRecord;
import org.apache.poi.hssf.record.BoundSheetRecord;
import org.apache.poi.hssf.record.FormulaRecord;
import org.apache.poi.hssf.record.LabelRecord;
import org.apache.poi.hssf.record.LabelSSTRecord;
import org.apache.poi.hssf.record.NoteRecord;
import org.apache.poi.hssf.record.NumberRecord;
import org.apache.poi.hssf.record.RKRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.SSTRecord;
import org.apache.poi.hssf.record.StringRecord;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class XlsSaxAnalyser extends BaseSaxAnalyser implements HSSFListener {
    private boolean analyAllSheet = false;
    private POIFSFileSystem fs;
    private int lastRowNumber;
    private int lastColumnNumber;
    private boolean outputFormulaValues = true;
    private SheetRecordCollectingListener workbookBuildingListener;
    private HSSFWorkbook stubWorkbook;
    private SSTRecord sstRecord;
    private FormatTrackingHSSFListener formatListener;
    private int nextRow;
    private int nextColumn;
    private boolean outputNextStringRecord;
    private int sheetIndex;
    private List<String> records;
    private boolean notAllEmpty = false;
    private BoundSheetRecord[] orderedBSRs;
    private List<BoundSheetRecord> boundSheetRecords = new ArrayList();
    private List<Sheet> sheets = new ArrayList();

    public XlsSaxAnalyser(AnalysisContext context) throws IOException {
        this.analysisContext = context;
        this.records = new ArrayList();
        if(this.analysisContext.getCurrentSheet() == null) {
            this.analyAllSheet = true;
        }

        context.setCurrentRowNum(Integer.valueOf(0));
        this.fs = new POIFSFileSystem(this.analysisContext.getInputStream());
    }

    public List<Sheet> getSheets() {
        this.execute();
        return this.sheets;
    }

    public void execute() {
        this.init();
        MissingRecordAwareHSSFListener listener = new MissingRecordAwareHSSFListener(this);
        this.formatListener = new FormatTrackingHSSFListener(listener);
        HSSFEventFactory factory = new HSSFEventFactory();
        HSSFRequest request = new HSSFRequest();
        if(this.outputFormulaValues) {
            request.addListenerForAllRecords(this.formatListener);
        } else {
            this.workbookBuildingListener = new SheetRecordCollectingListener(this.formatListener);
            request.addListenerForAllRecords(this.workbookBuildingListener);
        }

        try {
            factory.processWorkbookEvents(request, this.fs);
        } catch (IOException var5) {
            throw new ExcelAnalysisException(var5);
        }
    }

    private void init() {
        this.lastRowNumber = 0;
        this.lastColumnNumber = 0;
        this.nextRow = 0;
        this.nextColumn = 0;
        this.sheetIndex = 0;
        this.records = new ArrayList();
        this.notAllEmpty = false;
        this.orderedBSRs = null;
        this.boundSheetRecords = new ArrayList();
        this.sheets = new ArrayList();
        if(this.analysisContext.getCurrentSheet() == null) {
            this.analyAllSheet = true;
        } else {
            this.analyAllSheet = false;
        }

    }

    public void processRecord(Record record) {
        int thisRow = -1;
        int thisColumn = -1;
        String thisStr = null;
        switch(record.getSid()) {
            case 6:
                FormulaRecord frec = (FormulaRecord)record;
                thisRow = frec.getRow();
                thisColumn = frec.getColumn();
                if(this.outputFormulaValues) {
                    if(Double.isNaN(frec.getValue())) {
                        this.outputNextStringRecord = true;
                        this.nextRow = frec.getRow();
                        this.nextColumn = frec.getColumn();
                    } else {
                        thisStr = this.formatListener.formatNumberDateCell(frec);
                    }
                } else {
                    thisStr = HSSFFormulaParser.toFormulaString(this.stubWorkbook, frec.getParsedExpression());
                }
                break;
            case 28:
                NoteRecord nrec = (NoteRecord)record;
                thisRow = nrec.getRow();
                thisColumn = nrec.getColumn();
                thisStr = "(TODO)";
                break;
            case 133:
                this.boundSheetRecords.add((BoundSheetRecord)record);
                break;
            case 252:
                this.sstRecord = (SSTRecord)record;
                break;
            case 253:
                LabelSSTRecord lsrec = (LabelSSTRecord)record;
                thisRow = lsrec.getRow();
                thisColumn = lsrec.getColumn();
                if(this.sstRecord == null) {
                    thisStr = "";
                } else {
                    thisStr = this.sstRecord.getString(lsrec.getSSTIndex()).toString();
                }
                break;
            case 513:
                BlankRecord brec1 = (BlankRecord)record;
                thisRow = brec1.getRow();
                thisColumn = brec1.getColumn();
                thisStr = "";
                break;
            case 515:
                NumberRecord numrec = (NumberRecord)record;
                thisRow = numrec.getRow();
                thisColumn = numrec.getColumn();
                thisStr = this.formatListener.formatNumberDateCell(numrec);
                break;
            case 516:
                LabelRecord lrec1 = (LabelRecord)record;
                thisRow = lrec1.getRow();
                thisColumn = lrec1.getColumn();
                thisStr = lrec1.getValue();
                break;
            case 517:
                BoolErrRecord berec = (BoolErrRecord)record;
                thisRow = berec.getRow();
                thisColumn = berec.getColumn();
                thisStr = "";
                break;
            case 519:
                if(this.outputNextStringRecord) {
                    StringRecord lrec = (StringRecord)record;
                    thisStr = lrec.getString();
                    thisRow = this.nextRow;
                    thisColumn = this.nextColumn;
                    this.outputNextStringRecord = false;
                }
                break;
            case 638:
                RKRecord rkrec = (RKRecord)record;
                thisRow = rkrec.getRow();
                thisColumn = rkrec.getColumn();
                thisStr = "";
                break;
            case 2057:
                BOFRecord sheet = (BOFRecord)record;
                if(sheet.getType() == 16) {
                    if(this.workbookBuildingListener != null && this.stubWorkbook == null) {
                        this.stubWorkbook = this.workbookBuildingListener.getStubHSSFWorkbook();
                    }

                    if(this.orderedBSRs == null) {
                        this.orderedBSRs = BoundSheetRecord.orderByBofPosition(this.boundSheetRecords);
                    }

                    ++this.sheetIndex;
                    Sheet brec = new Sheet(this.sheetIndex, 0);
                    brec.setSheetName(this.orderedBSRs[this.sheetIndex - 1].getSheetname());
                    this.sheets.add(brec);
                    if(this.analyAllSheet) {
                        this.analysisContext.setCurrentSheet(brec);
                    }
                }
        }

        if(thisRow != -1 && thisRow != this.lastRowNumber) {
            this.lastColumnNumber = -1;
        }

        if(record instanceof MissingCellDummyRecord) {
            MissingCellDummyRecord sheet1 = (MissingCellDummyRecord)record;
            thisRow = sheet1.getRow();
            thisColumn = sheet1.getColumn();
            thisStr = "";
        }

        if(thisStr != null) {
            if(this.analysisContext.trim()) {
                thisStr = thisStr.trim();
            }

            if(!"".equals(thisStr)) {
                this.notAllEmpty = true;
            }

            this.records.add(thisStr);
        }

        if(thisRow > -1) {
            this.lastRowNumber = thisRow;
        }

        if(thisColumn > -1) {
            this.lastColumnNumber = thisColumn;
        }

        if(record instanceof LastCellOfRowDummyRecord) {
            thisRow = ((LastCellOfRowDummyRecord)record).getRow();
            if(this.lastColumnNumber == -1) {
                this.lastColumnNumber = 0;
            }

            this.analysisContext.setCurrentRowNum(Integer.valueOf(thisRow));
            Sheet sheet2 = this.analysisContext.getCurrentSheet();
            if((sheet2 == null || sheet2.getSheetNo() == this.sheetIndex) && this.notAllEmpty) {
                this.notifyListeners(new OneRowAnalysisFinishEvent(this.records));
            }

            this.records.clear();
            this.lastColumnNumber = -1;
            this.notAllEmpty = false;
        }

    }
}
