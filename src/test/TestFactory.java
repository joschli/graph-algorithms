package test;

import java.util.ArrayList;
import java.util.List;

public class TestFactory {

	public static Test createVerySmallTestCase() {
		List<Triplet> triplets = new ArrayList<>();
		triplets.add(new Triplet(1, 6, 20));
		return new Test(triplets);
	}

	public static Test createSmallTestCase() {
		List<Triplet> triplets = new ArrayList<>();
		triplets.add(new Triplet(1, 6, 20));
		triplets.add(new Triplet(3, 10, 40));
		triplets.add(new Triplet(2, 15, 100));
		return new Test(triplets);
	}

	public static Test createMediumTestCase() {
		List<Triplet> triplets = new ArrayList<>();
		triplets.add(new Triplet(50, 10, 100));
		triplets.add(new Triplet(50, 15, 100));
		triplets.add(new Triplet(50, 20, 100));
		return new Test(triplets);
	}

	public static Test createMediumBigTestCase() {
		List<Triplet> triplets = new ArrayList<>();
		triplets.add(new Triplet(1000, 10, 20));
		return new Test(triplets);
	}

	public static Test createMediumBigTestCase1() {
		List<Triplet> triplets = new ArrayList<>();
		triplets.add(new Triplet(1000, 15, 20));
		return new Test(triplets);
	}

	public static Test createMediumBigTestCase2() {
		List<Triplet> triplets = new ArrayList<>();
		triplets.add(new Triplet(500, 20, 20));
		return new Test(triplets);
	}

	public static Test createMediumBigTestCase3() {
		List<Triplet> triplets = new ArrayList<>();
		triplets.add(new Triplet(500, 25, 20));
		return new Test(triplets);
	}

	public static Test createBigTestCase() {
		List<Triplet> triplets = new ArrayList<>();
		triplets.add(new Triplet(100, 100, 1000));
		triplets.add(new Triplet(100, 150, 2000));
		triplets.add(new Triplet(100, 200, 3000));
		return new Test(triplets);
	}
}
