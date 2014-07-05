package de.horstfestival.android;
 
public class NavDrawerItem {
     
    private String title;
    private String icon;
     
    public NavDrawerItem(){}
 
    public NavDrawerItem(String title, String icon){
        this.title = title;
        this.icon = icon;
    }
     
    public String getTitle(){
        return this.title;
    }
     
    public String getIcon(){
        return this.icon;
    }
  
    public void setTitle(String title){
        this.title = title;
    }
     
    public void setIcon(String icon){
        this.icon = icon;
    }
}