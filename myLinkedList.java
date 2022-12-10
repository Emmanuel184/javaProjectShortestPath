import java.util.Iterator; //used for implementing iterator

/*
 *  My linked list class, a basic linked list, changed equal method to compare the data held at the head of two linked lists.
 *      Besides that You can also choose to insert at head or tail of our linked list, inserting at head helps when we implement 
 *      our stack, and inserting at tail helps when just creating a normal list and match the graph in the project doc.
 */
public class myLinkedList<E> implements Iterable<E> {

    private node<E> head;

    private int length;

    public myLinkedList(E city) {
        this.head = new node<>(city);
        this.length = 1;
    }

    public myLinkedList() {
        this.head = null;
        this.length = 0;
    }

    public int getLength() {
        return this.length;
    }

    public void addAtHead(E elemenet) {

        node<E> newNode = new node<>(elemenet);

        // check if first element in our linked list
        if (head == null) {
            this.head = newNode;
        } else {

            newNode.next = this.head;
            this.head = newNode;
        }

        this.length++;
    }

    public void addAtTail(E element) {

        node<E> newNode = new node<>(element);

        if (this.head == null) {
            this.head = newNode;
        } else {
            node<E> currentNode = this.head;

            while (currentNode.next != null) {
                currentNode = currentNode.next;
            }

            currentNode.next = newNode;
        }

        this.length++;
    }

    public E remove(int index) {

        node<E> previosNode = null;
        node<E> currentNode = this.head;

        if (index < 0 || index > this.length) {
            System.out.println("INDEX OUT OF BOUNDS");
            return null;
        }

        if (index == 0) {
            this.head = currentNode.next;

        } else {

            for (int i = 0; i < index; i++) {
                previosNode = currentNode;
                currentNode = currentNode.next;
            }

            previosNode.next = currentNode.next;
        }

        this.length--;

        return currentNode.element;

    }

    // checks if element is inlist by iterating through each node, and return if
    // found or not
    public boolean isInList(E element) {

        if (this.head == null) {
            return false;
        }

        node<E> currentNode = this.head;

        while (currentNode.next != null && !(currentNode.element.equals(element))) {
            currentNode = currentNode.next;
        }

        if (currentNode.element.equals(element)) {
            return true;
        } else {
            return false;
        }
    }

    // passes element and getsIndex based on head = 0 index and last node is length
    // - 1 index, helps with arrays when implementing
    // dijkstras
    public int getIndex(E element) {

        node<E> newNode = new node<E>(element);
        node<E> currentNode = this.head;

        int returnIndex = 0;

        if (this.head.element.equals(newNode.element)) {
            return 0;
        } else {

            while (currentNode.next != null && !(currentNode.element.equals(element))) {
                currentNode = currentNode.next;
                returnIndex++;

            }

        }

        if (currentNode.element.equals(element)) {
            return returnIndex;
        } else {
            return -1;
        }

    }

    public E get(E element) {

        if (this.head.element.equals((element))) {
            return this.head.element;
        }

        node<E> returnNode = this.head;

        while (returnNode.next != null && !(returnNode.element.equals(element))) {

            returnNode = returnNode.next;

        }

        if (!(returnNode.element.equals(element))) {
            return null;
        } else {
            return returnNode.element;
        }

    }

    public String toString() {

        String returnString = "";

        node<E> currentNode = this.head;

        while (currentNode.next != null) {
            returnString += currentNode.element + "\n";
            currentNode = currentNode.next;
        }

        return returnString;
    }

    //This helps when comparing cities, basically we compare two linked lists based on their head element, this helps becuse
    //each linked list head is the city the linked list represents in our graph
    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null) {
            return false;
        }

        if (getClass() != o.getClass()) {
            return false;
        }

        myLinkedList<city> cityLinkedList = (myLinkedList) o;

        return this.head.element.equals(cityLinkedList.head.element);

    }

    //iterator to be able to use for each
    @Override
    public Iterator<E> iterator() {
        // TODO Auto-generated method stub
        return new linkedListIterable(this);
    }

    public node<E> getHead() {
        return this.head;
    }

}

//node class for our linked list
class node<E> {

    E element;

    node<E> next;

    node(E element) {
        this.element = element;
        this.next = null;
    }

}

/*
 *  iterator for linked list, to be able to use for each...basically keep going to next node aslong as the currentNode is not null
 */
class linkedListIterable<E> implements Iterator<E> {

    node<E> currentNode;

    linkedListIterable(myLinkedList<E> linkedList) {
        this.currentNode = linkedList.getHead();
    }

    public boolean hasNext() {
        return currentNode != null;
    }

    public E next() {
        E returnElement = currentNode.element;
        currentNode = currentNode.next;
        return returnElement;
    }

}