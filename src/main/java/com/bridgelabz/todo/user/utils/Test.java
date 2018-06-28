package com.bridgelabz.todo.user.utils;

public class Test {

	public static void main(String[] args) {
		String pattern = "^[0-9]{10}$";
		
		String number = "9224260800";
		
		System.out.println(number.matches(pattern));
	}
}
