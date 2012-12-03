/**
 * Copyright (c) 2012 Jason Reddekopp, Andrew McCann, Daniel Sopel, David Yu. All rights reserved. This program and the accompanying materials are made available under the terms of the GNU Public License v3.0 which accompanies this distribution, and is available at http://www.gnu.org/licenses/gpl.html Contributors: Jason Reddekopp, Andrew McCann, Daniel Sopel, David Yu - initial API and                               implementation
 */
package com.example.cmput301.model;
import java.util.Comparator;  

/**
 * This Comparator class is used as a Comparator argument in Collections.sort to sort tasks according to the number of votes.
 * ie. Collections.sort(tasks, new VotesComparator());
 * Without this argument, the tasks collection is sorted by id.
 */
public class VotesComparator implements Comparator<Task>  
{  
   public int compare(Task t1, Task t2)  
   {  
      return t1.getVotes() > t2.getVotes() ?   -1  :  
             t2.getVotes() > t1.getVotes() ?  1  :  
             0;  
   }  
} 