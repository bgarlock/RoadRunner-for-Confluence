package test;

import java.io.*;
import java.awt.datatransfer.*;
import java.util.*;

/** This class stores all family data that we use in the example. You should
note that no error checking is done, but you probably will want to set that up
in your scheme especially if you are using beans! */
public class PersonalInfo implements Transferable, Serializable {

  final public static DataFlavor INFO_FLAVOR =
    new DataFlavor(PersonalInfo.class, "Personal Information");

  static DataFlavor flavors[] = {INFO_FLAVOR };

  public static final boolean MALE = true;
  public static final boolean FEMALE = !MALE;

  private String Name = null;
  private boolean Gender = MALE;
  private PersonalInfo Parent = null;
  private Vector Children = null;
  

  public PersonalInfo(String name, boolean gender) {
    Children = new Vector();
    Name = name;
    Gender = gender;
  }

  public String getName() {
    return Name;
  }

  public void setName(String name) {
    Name = name;
  }

  public boolean getGender() {
    return Gender;
  }

  public void setGender(boolean gender) {
    Gender = gender;
  }

  public boolean isMale() {
    return getGender() == MALE;
  }

  public void add(PersonalInfo info) {
    info.setParent(this);
    Children.add(info);
  }

  public void remove(PersonalInfo info) {
    info.setParent(null);
    Children.remove(info);
  }

  public PersonalInfo getParent() {
    return Parent;
  }

  public void setParent(PersonalInfo parent) {
    Parent = parent;
  }

  public Vector getChildren() {
    return Children;
  }

  public Object clone() {
    return new PersonalInfo(Name, Gender);
  }

  public String toString() {
    return Name;
  }

  // --------- Transferable --------------

  public boolean isDataFlavorSupported(DataFlavor df) {
    return df.equals(INFO_FLAVOR);
  }

  /** implements Transferable interface */
  public Object getTransferData(DataFlavor df)
      throws UnsupportedFlavorException, IOException {
    if (df.equals(INFO_FLAVOR)) {
      return this;
    }
    else throw new UnsupportedFlavorException(df);
  }

  /** implements Transferable interface */
  public DataFlavor[] getTransferDataFlavors() {
    return flavors;
  }

  // --------- Serializable --------------

   private void writeObject(java.io.ObjectOutputStream out) throws IOException {
     out.defaultWriteObject();
   }

   private void readObject(java.io.ObjectInputStream in)
     throws IOException, ClassNotFoundException {
     in.defaultReadObject();
   }
}
