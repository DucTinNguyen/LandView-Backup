package com.example.landview.UserFragmentSection;

public class PlanItem {
    private String name;
    private String destination;
    private String id;
    private String description;
    private String dateStart;
    private String dateEnd;

    //Constructor
    public PlanItem(String name, String description, String destination, String id, String dateEnd, String dateStart){
        this.name = name;
        this.description = description;
        this.destination = destination;
        this.id = id;
        this.dateEnd = dateEnd;
        this.dateStart = dateStart;
    }

    //Getter
    public String getName(){return name;}

    public String getDestination(){return destination;}

    public String getDescription(){return description;}

    public String getId(){return id;}

    public String getDateStart(){return dateStart;}

    public String getDateEnd(){return dateEnd;}

    //Setter
    public void setName(String name){this.name=name;}

    public void setId(String id){this.id=id;}

    public void setDestination(String destination){this.destination=destination;}

    public void setDescription(String description){this.description=description;}

    public void setDateEnd(String dateEnd){this.dateEnd=dateEnd;}

    public void setDateStart(String dateStart){this.dateStart=dateStart;}
}
