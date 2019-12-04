package org.insightcentre.saffron.web.api;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;

@XmlRootElement()
public class AuthorTermsResponse {

    private double similarity;
    private String id;
    private String authorTerm;
    private String termString;
    private Integer matches;
    private Integer occurences;
    private Double score;
    private List<String> mvList;
    private Date runDate;
    private String run;
    private String dbpediaUrl;



    public void setAuthorTerm(String authorTerm) {
        this.authorTerm = authorTerm;
    }

    public String getAuthorTerm() {
        return authorTerm;
    }

    public void setTermString(String termString) {
        this.termString = termString;
    }

    public String getTermString() {
        return termString;
    }


    public void setScore(Double score) {
        this.score = score;
    }

    public Double getScore() {
        return score;
    }

    public void setMvList(List<String> mvList) {
        this.mvList = mvList;
    }

    public List<String> getMvList() {
        return mvList;
    }

    public void setRunDate(Date runDate) {
        this.runDate = runDate;
    }

    public Date getRunDate() {
        return runDate;
    }

    public void setRun(String run) {
        this.run = run;
    }

    public String getRun() {
        return run;
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setOccurrences(Integer occurrences) {
        this.occurences = occurrences;
    }

    public Integer getOccurances() {
        return occurences;
    }

    public void setMatches(Integer matches) {
        this.matches = matches;
    }

    public Integer getMatches() {
        return matches;
    }

    public void setDbpediaUrl(String dbpediaUrl) {
        this.dbpediaUrl = dbpediaUrl;
    }

    public String getDbpediaUrl() {
        return dbpediaUrl;
    }
}