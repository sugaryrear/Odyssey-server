package io.Odyssey.model.collisionmap.doors;

import io.Odyssey.Server;
import io.Odyssey.model.collisionmap.ObjectDef;
import io.Odyssey.model.collisionmap.RegionProvider;
import io.Odyssey.model.collisionmap.WorldObject;
import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.world.objects.GlobalObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Doors {

    private static Doors singleton = null;

    private List<Doors> doors = new ArrayList<>();

    private File doorFile;

    public static Doors getSingleton() {//this is such a good way to do this.
        if (singleton == null) {
            singleton = new Doors("./etc/cfg/doors/doors.txt");
        }
        return singleton;
    }

    private Doors(String file){
        doorFile = new File(file);
    }

    private Doors(int door, int x, int y, int z, int face, int type, int open) {
        this.doorId = door;
        this.originalId = door;
        this.doorX = x;
        this.doorY = y;
        this.originalX = x;
        this.originalY = y;
        this.doorZ = z;
        this.originalFace = face;
        this.currentFace = face;
        this.type = type;
        this.open = open;
    }

    private Doors getDoor(int id, int x, int y, int z) {
        for (Doors d : doors) {
            if (d.doorId == id) {
                if (d.doorX == x && d.doorY == y && d.doorZ == z) {
                    return d;
                }
            }
        }
        return null;
    }

    public boolean handleDoor(GlobalObject object, int id, int x, int y, int z) {
 Optional<WorldObject> object_real = object.getRegionProvider().get(x, y).getWorldObject(id, x, y, z);
//if(object_real.isPresent()){
//    System.out.println("it is.");
//}
//        /**
//         * Debug
//         */
//        //player.sendMessage("Chance="+playerChance+" rolled="+chance+" slashBonus="+slashBonus+" FaceDirection="+obj.get().getFace()+" - "+obj.get().type);
//
//        /**
//         * Boolean check for when the player is sucessful.
//         */
//        if (playerChance >= chance) {
//            player.sendMessage("You slash the web apart.");
//            Server.getGlobalObjects().add(new GlobalObject(734, objectX, objectY, obj.get().getHeight(), obj.get().getFace(), obj.get().getType(), 20, 733));
//        } else
//            player.sendMessage("You fail to cut through the web.");
//    }

        ObjectDef def = ObjectDef.getObjectDef(object.getObjectId());

       // System.out.println("def.n: "+def.name);
        if ((def != null ? def.name : null) != null && def.name.toLowerCase().contains("door")) {
//
       //     System.out.println("solid: "+def.aBoolean767+" impenetrable: "+def.aBoolean757+" shad: "+def.aBoolean779);
        Doors d = getDoor(id, x, y, z);
        if (d == null) {
            if (DoubleDoors.getSingleton().handleDoor( id, x, y, z)) {
                return true;
            }
            return false;
        }
//            id = object.getObjectId();
//            x = x;
//            y = y;
//            z = z;
//            int face = object.getFace();
//            int type = object.getType();
//            Doors d = new Doors(id, x, y, z, face, 0, 0);
//            System.out.println("door face: " + face + "  and type: " + type);
            int xAdjustment = 0, yAdjustment = 0;
            if (d.type == 0) {
                if (d.open == 0) {//originally closed door
                    if (d.originalFace == 0 && d.currentFace == 0) {
                        xAdjustment = -1;
                    } else if (d.originalFace == 1 && d.currentFace == 1) {
                        yAdjustment = 1;
                    } else if (d.originalFace == 2 && d.currentFace == 2) {
                        xAdjustment = 1;
                    } else if (d.originalFace == 3 && d.currentFace == 3) {
                        yAdjustment = -1;
                    }
                } else if (d.open == 1) {
                    if (d.originalFace == 0 && d.currentFace == 0) {
                        yAdjustment = 1;
                    } else if (d.originalFace == 1 && d.currentFace == 1) {
                        xAdjustment = 1;
                    } else if (d.originalFace == 2 && d.currentFace == 2) {
                        yAdjustment = -1;
                    } else if (d.originalFace == 3 && d.currentFace == 3) {
                        xAdjustment = -1;
                    }
                }
            } else if (d.type == 9) {
                if (d.open == 0) {
                    if (d.originalFace == 0 && d.currentFace == 0) {
                        xAdjustment = 1;
                    } else if (d.originalFace == 1 && d.currentFace == 1) {
                        xAdjustment = 1;
                    } else if (d.originalFace == 2 && d.currentFace == 2) {
                        xAdjustment = -1;
                    } else if (d.originalFace == 3 && d.currentFace == 3) {
                        xAdjustment = -1;
                    }
                } else if (d.open == 1) {
                    if (d.originalFace == 0 && d.currentFace == 0) {
                        xAdjustment = 1;
                    } else if (d.originalFace == 1 && d.currentFace == 1) {
                        xAdjustment = 1;
                    } else if (d.originalFace == 2 && d.currentFace == 2) {
                        xAdjustment = -1;
                    } else if (d.originalFace == 3 && d.currentFace == 3) {
                        xAdjustment = -1;
                    }
                }
            }
            if (xAdjustment != 0 || yAdjustment != 0) {

              //  GlobalObject o = new GlobalObject(-1, d.doorX, d.doorY, d.doorZ, d.originalFace, 0);//remove it? yes.
             //   Server.getGlobalObjects().add(o);
            //    System.out.println("id: "+object_real.get().id+" x:"+object_real.get().x+" y: "+object_real.get().y+" f: "+object_real.get().face+"  type: "+object_real.get().type);
            //    System.out.println("from door file: id: "+d.doorId+" x:"+d.doorX+" y: "+d.doorY+" f: "+d.currentFace+"  type: "+d.type);
//Optional<WorldObject> thedoor = object.getRegionProvider().get(c.absX, c.absY).getWorldObject(objectType,obX,obY,c.getHeight());
//GlobalObject theobject1560 = new GlobalObject(object_real1560.get().id, object_real1560.get().x, object_real1560.get().y, object_real1560.get().height, object_real1560.get().face, object_real1560.get().type);



GlobalObject theobject = new GlobalObject( object_real.get().id, object_real.get().x, object_real.get().y, object_real.get().height, object_real.get().face, 0);
 Server.getGlobalObjects().add_door(theobject);
//Server.getGlobalObjects().remove(theobject);
              //  Server.getGlobalObjects().updateObject(theobject, object_real.get().id);
            }
            if (d.doorX == d.originalX && d.doorY == d.originalY) {
                d.doorX += xAdjustment;
                d.doorY += yAdjustment;
            } else {
                GlobalObject theobject = new GlobalObject( object_real.get().id, object_real.get().x, object_real.get().y, object_real.get().height, object_real.get().face, 0);
                Server.getGlobalObjects().add_door(theobject);

//                GlobalObject theobject = new GlobalObject(object_real.get().id, object_real.get().x, object_real.get().y, object_real.get().height, object_real.get().face, object_real.get().type);
//                Server.getGlobalObjects().add(theobject);
//                Server.getGlobalObjects().remove(theobject);
               // Server.getGlobalObjects().updateObject(theobject, object_real.get().id);

                d.doorX = d.originalX;
                d.doorY = d.originalY;
            }
            if (d.doorId == d.originalId) {
                if (d.open == 0) {
                    d.doorId += 1;
                } else if (d.open == 1) {
                    d.doorId -= 1;
                }
            } else if (d.doorId != d.originalId) {
                if (d.open == 0) {
                    d.doorId -= 1;
                } else if (d.open == 1) {
                    d.doorId += 1;
                }
            }

            //     Server.getGlobalObjects().add(new GlobalObject(d.doorId, d.doorX, d.doorY, d.doorZ, getNextFace(d), 0));
            Server.getGlobalObjects().add(new GlobalObject(d.doorId, d.doorX, d.doorY, d.doorZ, getNextFace(d), 0));

            //System.out.println("the face it should be: "+getNextFace(d));


        }  return false;
    }

    private int getNextFace(Doors d) {
        int f = d.originalFace;
      //  System.out.println("d.origin: "+d.originalFace+" and cu: "+d.currentFace);
        if (d.type == 0) {
            if (d.open == 0) {
                if (d.originalFace == 0 && d.currentFace == 0) {
                    f = 1;
                } else if (d.originalFace == 1 && d.currentFace == 1) {
                    f = 2;
                } else if (d.originalFace == 2 && d.currentFace == 2) {
                    f = 3;
                } else if (d.originalFace == 3 && d.currentFace == 3) {
                    f = 0;
                } else if (d.originalFace != d.currentFace){
                    f = d.originalFace;
                }
            } else if (d.open == 1) {
                if (d.originalFace == 0 && d.currentFace == 0) {
                    f = 3;
                } else if (d.originalFace == 1 && d.currentFace == 1) {
                    f = 0;
                } else if (d.originalFace == 2 && d.currentFace == 2) {
                    f = 1;
                } else if (d.originalFace == 3 && d.currentFace == 3) {
                    f = 2;
                } else if (d.originalFace != d.currentFace){
                    f = d.originalFace;
                }
            }
        } else if (d.type == 9) {
            if (d.open == 0) {
                if (d.originalFace == 0 && d.currentFace == 0) {
                    f = 3;
                } else if (d.originalFace == 1 && d.currentFace == 1) {
                    f = 2;
                } else if (d.originalFace == 2 && d.currentFace == 2) {
                    f = 1;
                } else if (d.originalFace == 3 && d.currentFace == 3) {
                    f = 0;
                } else if (d.originalFace != d.currentFace){
                    f = d.originalFace;
                }
            } else if (d.open == 1) {
                if (d.originalFace == 0 && d.currentFace == 0) {
                    f = 3;
                } else if (d.originalFace == 1 && d.currentFace == 1) {
                    f = 0;
                } else if (d.originalFace == 2 && d.currentFace == 2) {
                    f = 1;
                } else if (d.originalFace == 3 && d.currentFace == 3) {
                    f = 2;
                } else if (d.originalFace != d.currentFace){
                    f = d.originalFace;
                }
            }
        }
        d.currentFace = f;
      //  System.out.println("new face: "+f);
        return f;
    }


    public void load() {
        long start = System.currentTimeMillis();
        try {
            singleton.processLineByLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Loaded "+ doors.size() +" doors in "+ (System.currentTimeMillis() - start) +"ms.");
    }

    public final void processLineByLine() throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileReader(doorFile));
        try {
            while(scanner.hasNextLine()) {
                processLine(scanner.nextLine());
            }
        } finally {
            scanner.close();
        }
    }

    protected void processLine(String line){
        Scanner scanner = new Scanner(line);
        scanner.useDelimiter(" ");
        try {
            while(scanner.hasNextLine()) {
                int id = Integer.parseInt(scanner.next());
                int x = Integer.parseInt(scanner.next());
                int y = Integer.parseInt(scanner.next());
                int f = Integer.parseInt(scanner.next());
                int z = Integer.parseInt(scanner.next());
                int t = Integer.parseInt(scanner.next());
                doors.add(new Doors(id,x,y,z,f,t,alreadyOpen(id)?1:0));//a door that starts as open is 1
            }
        } finally {
            scanner.close();
        }
    }

    private boolean alreadyOpen(int id) {
        for (int i = 0; i < openDoors.length; i++) {
            if (openDoors[i] == id) {
                return true;
            }
        }
        return false;
    }

    private int doorId;
    private int originalId;
    private int doorX;
    private int doorY;
    private int originalX;
    private int originalY;
    private int doorZ;
    private int originalFace;
    private int currentFace;
    private int type;
    private int open;

    private static int[] openDoors = {
            1504, 1514, 1517, 1520, 1531,
            1534, 1536, 1541, 2033, 2035, 2037, 2998,
            3271, 4468, 4697, 6101,6103,
            6105, 6107, 6109, 6111, 6113,
            6115, 6976, 6978, 8696, 8819,
            10261, 10263,10265,11708,11710,
            11712,11715,11994,12445, 13002,	11774,
    };

}
