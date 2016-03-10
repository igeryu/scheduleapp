// Person.java
/**
 * Changelog:
 * 2016-02-24 : Added rank, workcenter, shift, skill strings
 * 
 * 2016-02-25 : Made rank, workcenter, shift, and skill private
 * 2016-02-25 : Made get/set methods for rank, workcenter, shift, and skill
 * 
 * 2016-02-27 : Changed setRank(), setShift() and setSkill() to also update rank_id, shift_id and skill_id, respectively
 * 2016-02-27 : Added setWorkcenter_ID() and setShift_ID()
 * 
 * 2016-02-28 : Added setAll() method
 * 2016-02-28 : Modified setWorkcenter() to match other setters (updates workcenter as well as workcenter_id)
 * 
 * 2016-03-08 : Added constructor that takes int values for rank, workcenter and skill level (IDs)
 * 2016-03-08 : Added setRankID() and setSkillID() to work in parallel with setWorkcenterID()
 */

/**
 *
 * @author Alan Johnson
 */
package domain;

import java.util.Map;

public class Person implements java.io.Serializable {

    int objectID;
    String firstName;
    String lastName;
    private String rank;
    private String workcenter;
    private String shift;
    private String skill;
    
    
    Integer rank_id;
    Integer workcenter_id;
    Integer shift_id;
    Integer skill_id;

    //  For testing purposes:
    public Person (String fn, String ln, String rk, String sk) {
        this(-1, fn, ln, 1, 1, 1, 1);
        setRank(rk);
        setSkill(sk);
    }
    
    public Person (String fn, String ln, int rk, int wc, int sk) {
        this(-1, fn, ln, 1, 1, 1, 1);
        setRankID(rk);
        setWorkcenterID(wc);
        setSkillID(sk);
    }

    //The full constructor is package-private to prevent misuse.
    public Person() {
        this(-1, "", "", 0, 0, 0, 0);
    }

    Person(int oID, String fn, String ln, int rID, int wcID, int shID, int skID) {
        objectID = oID;
        firstName = fn;
        lastName = ln;
        rank_id = rID;
        setWorkcenterID(wcID);
        setShiftID(shID);
        skill_id = skID;
    }
    
    public void setAll(String fn, String ln, String rn, String wc, String sh, String sk) {
        setFirstName(fn);
        setLastName(ln);
        setRank(rn);
        setWorkcenter(wc);
        setShift(sh);
        setSkill(sk);
    }

    public void setFirstName(String n) {
        if (n != null && !n.equals("")) {
            firstName = n;
        }
    }

    public String getFirstName() {
        return firstName;
    }
    
    public void setLastName(String n) {
        if (n != null && !n.equals("")) {
            lastName = n;
        }
    }

    public String getLastName() {
        return lastName;
    }

    public void setObjectID(int id) {
        objectID = id;
    }

    public int getObjectID() {
        return objectID;
    }

    public int getRankID() {
        return rank_id;
    }

    public int getShiftID() {
        return shift_id;
    }

    public int getWorkcenterID() {
        return workcenter_id;
    }

    public int getSkillID() {
        return skill_id;
    }

    public String toString() {
        return String.format("(ID: %s) %s, %s: (R: %s) (W: %s) (S: %s)",
                objectID, firstName, lastName,
                rank_id, workcenter_id, shift_id);
    }

    /**
     * @return the rank
     */
    public String getRank() {
        return rank;
    }

    /**
     * @return the shift
     */
    public String getShift() {
        return shift;
    }

    /**
     * @return the skill
     */
    public String getSkill() {
        return skill;
    }

    /**
     * @return the workcenter
     */
    public String getWorkcenter() {
        return workcenter;
    }

    /**
     * @param workcenter the workcenter to set
     */
    public void setWorkcenter(String workcenter) {
        this.workcenter = workcenter;
        
        Map<String, Integer> mapReversed = (new WorkcenterDAO()).getMapReversed();
        workcenter_id = mapReversed.get(workcenter);
    }
    
    private void setWorkcenterID (Integer workcenter_id) {
        this.workcenter_id = workcenter_id;
        
        Map<Integer, String> map = (new WorkcenterDAO()).getMap();
        workcenter = map.get(workcenter_id);
    }

    /**
     * Should exist
     * 
     * @param rank the rank to set
     */
    public void setRank(String rank) {
        this.rank = rank;
        
        Map<String, Integer> mapReversed = (new RankDAO()).getMapReversed();
        rank_id = mapReversed.get(rank);
    }
    
    /**
     * Should exist
     * 
     * @param rank the rank to set
     */
    public void setRankID(int rID) {
        this.rank_id = rID;
        
        Map<Integer, String> map = (new RankDAO()).getMap();
        rank = map.get(rank_id);
    }

    /**
     * @param shift the shift to set
     */
    public void setShift(String shift) {
        this.shift = shift;
        
        Map<String, Integer> mapReversed = (new ShiftDAO()).getMapReversed();
        skill_id = mapReversed.get(skill);
    }
    
    private void setShiftID (Integer shift_id) {
        this.shift_id = shift_id;
        
        Map<Integer, String> map = (new ShiftDAO()).getMap();
        shift = map.get(shift_id);
    }

    /**
     * @param skill the skill to set
     */
    public void setSkill(String skill) {
        this.skill = skill;
        
        Map<String, Integer> mapReversed = (new SkillDAO()).getMapReversed();
        skill_id = mapReversed.get(skill);
    }
    
    public void setSkillID(int skID) {
        this.skill_id = skID;
        
        Map<Integer, String> map = (new SkillDAO()).getMap();
        skill = map.get(skill_id);
    }
}
