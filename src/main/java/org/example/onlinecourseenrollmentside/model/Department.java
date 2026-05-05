package org.example.onlinecourseenrollmentside.model;

import java.util.ArrayList;
import java.util.List;

public class Department {
	private final String name;
	private final List<Integer> courseIds;
	private final List<Integer> instructorIds;

	public Department(String name) {
		this(name, new ArrayList<>(), new ArrayList<>());
	}

	public Department(String name, List<Integer> courseIds, List<Integer> instructorIds) {
		this.name = name == null ? "" : name.trim();
		this.courseIds = new ArrayList<>(courseIds);
		this.instructorIds = new ArrayList<>(instructorIds);
	}

	public String getName() {
		return name;
	}

	public List<Integer> getCourseIds() {
		return new ArrayList<>(courseIds);
	}

	public List<Integer> getInstructorIds() {
		return new ArrayList<>(instructorIds);
	}

	public boolean addCourseId(int courseId) {
		if (courseIds.contains(courseId)) {
			return false;
		}
		courseIds.add(courseId);
		return true;
	}

	public boolean addInstructorId(int instructorId) {
		if (instructorIds.contains(instructorId)) {
			return false;
		}
		instructorIds.add(instructorId);
		return true;
	}

	@Override
	public String toString() {
		return name;
	}
}
