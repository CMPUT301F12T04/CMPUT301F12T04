/*
 * 
	Copyright 2012 Daniel Sopel
	This file is part of Foobar.

    Foobar is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Foobar is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.example.cmput301;

public class MainController {
	private TaskManager taskManager;
	
	public void mainController(TaskManager taskManager) {
		this.taskManager = taskManager;
	}
	
	public void addTask (String titleInput, String descInput) {
		 new Task(titleInput, descInput);
	}
}