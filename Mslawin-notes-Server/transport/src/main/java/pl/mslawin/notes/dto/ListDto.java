package pl.mslawin.notes.dto;

import java.util.List;

public class ListDto {

    private List<TaskListDto> taskListDtoList;

    public ListDto() {
    }

    public ListDto(List<TaskListDto> taskListDtoList) {
        this.taskListDtoList = taskListDtoList;
    }

    public List<TaskListDto> getTaskListDtoList() {
        return taskListDtoList;
    }

    public void setTaskListDtoList(List<TaskListDto> taskListDtoList) {
        this.taskListDtoList = taskListDtoList;
    }
}
