import queue.dog.DogQueue;

public class PlayWithDogs {

    public static void main(String[] args) {
        DogQueue dogQueue = new DogQueue("Jim", 5);
        for (int i = 0; i < 60; i++) {
            dogQueue.sit("Buddy-" + i);
        }
        dogQueue.done();
    }
}
