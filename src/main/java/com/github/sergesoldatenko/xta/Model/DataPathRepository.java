package com.github.sergesoldatenko.xta.Model;

class DataPathRepository {
    private XtDataHeader dataHeader;
    
    DataPathRepository(XtDataHeader dataHeader) {
        this.dataHeader = dataHeader;
    }

    public DataPath loadById(long id) {
        //long parentId;
        XtDataRecord xtDataRecord;
        DataPathElement parentPathElement, pathElement = null;
        DataPath path = new DataPath();
        XtDataRecordRepository xtDataRecordRepo = new XtDataRecordRepository(dataHeader);
        try {
            do {
                xtDataRecord = xtDataRecordRepo.loadById(id);
                parentPathElement = new DataPathElement(xtDataRecord.getIncrementId());
                if (pathElement != null) {
                    parentPathElement.setChild(pathElement);
                }
                pathElement = parentPathElement;
                id = xtDataRecord.getParent();
            } while (id != 0);
            path.setRootElement(pathElement);
        } catch (Exception ex) {
            System.getLogger(DataPathRepository.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

        return path;
    }
}
