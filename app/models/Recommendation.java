package models;

/**
 * Created by carol on 8/04/15.
 */
public class Recommendation {

    public Movie movie;
    public double estimatedRating;

    public Recommendation()
    {}
    public Recommendation(Movie b,double r)
    {
        movie=b;
        estimatedRating =r;
    }
    public double getEstimatedRating() {
        return estimatedRating;
    }

    public void setEstimatedRating(double estimatedRating) {
        this.estimatedRating = estimatedRating;
    }

    public Movie getBusiness() {
        return movie;
    }

    public void setBusiness(Movie business) {
        this.movie = business;
    }
}
