package codeTigers.BusinessLogic;

import java.awt.image.BufferedImage;

/**
 * Class to create the item pairs for the test
 *
 * @author Wally Haven
 * @version 10/24/2017.
 */
public class TestQuestion {
    private final Integer firstItemID;
    private final Integer secondItemID;
    private Integer score;

    private final String firstItemName;
    private final String secondItemName;

    private final BufferedImage image1;
    private final BufferedImage image2;


    public TestQuestion(Integer firstItemID, String firstItemName, BufferedImage image1,
                        Integer secondItemID, String secondItemName, BufferedImage image2) {
        this.firstItemID = firstItemID;
        this.secondItemID = secondItemID;
        this.firstItemName = firstItemName;
        this.secondItemName = secondItemName;
        this.image1 = image1;
        this.image2 = image2;
        score = 99;
    }


    public Integer getFirstItemID() {
        return firstItemID;
    }


    public Integer getSecondItemID() {
        return secondItemID;
    }

    public String getFirstItemName() {
        return firstItemName;
    }

    public String getSecondItemName() {
        return secondItemName;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public BufferedImage getImage1() {
        return image1;
    }

    public BufferedImage getImage2() {
        return image2;
    }

}
