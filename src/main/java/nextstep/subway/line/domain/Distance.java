package nextstep.subway.line.domain;


import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    private static final int MIN = 0;
    @Column(nullable = false)
    private int distance;

    protected Distance() {
    }

    private Distance(int distance) {
        validateDistance(distance);
        this.distance = distance;
    }

    public static Distance valueOf(int distance) {
        return new Distance(distance);
    }

    private void validateDistance(int distance) {
        if (isZeroOrLess(distance)) {
            throw new IllegalArgumentException("구간 길이는 0 이하가 될 수 없습니다.");
        }
    }

    private boolean isZeroOrLess(int distance) {
        return distance <= MIN;
    }

    public void minus(Distance distance) {
        this.distance = Math.subtractExact(this.distance, distance.distance());
        validateDistance(this.distance);
    }

    public void plus(Distance distance) {
        this.distance = Math.addExact(this.distance, distance.distance());
    }

    public int distance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Distance distance1 = (Distance) o;
        return distance == distance1.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }

    @Override
    public String toString() {
        return "Distance{" +
                "distance=" + distance +
                '}';
    }
}
