package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Task {

    private Manager creator; //Manager who created the task
    private Staff assignee; //Staff member assigned to the task

    private String name; //Name of task
    private String desc; //Description of task

    private LocalDateTime deadline; //Date and time of task due

    private boolean assigned; //Is this task assigned to anyone?
    private boolean completed; //Has this task been completed?
    private boolean accepted; //Has this task been accepted by an assignee?

    private String comments; //Comments added when completed
    private LocalDateTime submitDateTime; //Date and time of submission


    //Constructor for creating new task manually, starts unassigned
    public Task (Manager creator, String name, String desc, LocalDateTime deadline) {

        this.creator = creator;
        this.name = name;
        this.desc = desc;
        this.deadline = deadline;
        assigned = false;
        completed = false;
        accepted = false;

    }

    //Constructor for loading task from file, takes all variables as input
    public Task (Manager creator, Staff assignee, String name, String desc, LocalDateTime deadline, boolean assigned, boolean completed, boolean accepted, String comments, LocalDateTime subDateTime) {

        this.creator = creator;
        this.assignee = assignee;
        this.name = name;
        this.desc = desc;
        this.deadline = deadline;
        this.assigned = assigned;
        this.completed = completed;
        this.accepted = accepted;
        this.comments = comments;
        this.submitDateTime = subDateTime;

    }

    //Getters
    public Manager getCreator() {
        return creator;
    }
    public Staff getAssignee() {
        return assignee;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return desc;
    }
    public LocalDateTime getDeadline() {
        return deadline;
    }
    public boolean isCompleted() {
        return completed;
    }
    public boolean isAssigned() {
        return assigned;
    }
    public boolean isAccepted() {
        return accepted;
    }

    //Completed specific getters
    public String getComments() {
        return comments;
    }
    public LocalDateTime getSubmitDateTime() {
        return submitDateTime;
    }

    //Get on time status of task
    public boolean onTime() {
        
        //If completed, compare submission time with deadline
        //Otherwise, compare current time with deadline
        if (completed) {
            return submitDateTime.isBefore(deadline);
        } else {
            return LocalDateTime.now().isBefore(deadline);
        }

    }

    //Assign employee to task if unassigned. Return success status.
    public boolean assign (Staff s) {

        if (assigned) return false;
        assignee = s;
        assignee.addTask(this);
        assigned = true;
        return true;

    }

    //Unassign task
    public void unassign () {

        //Remove task from asignee's task list, 
        assignee.removeTask(this);
        assignee = null;
        assigned = false;
        accepted = false;

    }

    //Complete task
    public void complete (String comments, LocalDateTime subDateTime) {
        completed = true;
        this.comments = comments;
        this.submitDateTime = subDateTime;
        assignee.removeTask(this);
    }

    //Set task's accepted status
    public void setAccepted (boolean status) {
        accepted = status;
    }

}
