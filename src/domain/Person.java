// Person.java
/**
 * Changelog:
 * 2016-02-24 : Added rank, workcenter, shift, skill strings
 * 
 * 2016-02-25 : Made rank, workcenter, shift, and skill private
 * 2016-02-25 : Made get/set methods for rank, workcenter, shift, and skill
 */
/**
 *
 * @author Alan Johnson
 */
package domain;

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
        rank = rk;
        skill = sk;
    }

    //The full constructor is package-private to prevent misuse.
    public Person() {
        this(-1, "", "", 0, 0, 0, 0);
    }

    Person(int oID, String fn, String ln, Integer rID, Integer wcID, Integer shID, Integer skID) {
        objectID = oID;
        firstName = fn;
        lastName = ln;
        rank_id = rID;
        workcenter_id = wcID;
        shift_id = shID;
        skill_id = skID;
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
        return String.format("%s, %s: (R: %s) (W: %s) (S: %s)",
                firstName, lastName,
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
    }

    /**
     * @param rank the rank to set
     */
    public void setRank(String rank) {
        this.rank = rank;
    }

    /**
     * @param shift the shift to set
     */
    public void setShift(String shift) {
        this.shift = shift;
    }

    /**
     * @param skill the skill to set
     */
    public void setSkill(String skill) {
        this.skill = skill;
    }
}
