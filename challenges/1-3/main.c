#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct DoublyLinkedNode {
  struct DoublyLinkedNode *next;
  struct DoublyLinkedNode *prev;
  char data[100];
};

struct DoublyLinkedList {
  struct DoublyLinkedNode *head;
  struct DoublyLinkedNode *tail;
};

struct DoublyLinkedNode *find(struct DoublyLinkedList *list, char *d) {
  struct DoublyLinkedNode *node = list->head;
  while (node != NULL) {
    if (strcmp(node->data, d) == 0) {
      return node;
    }
    node = node->next;
  }
  return NULL;
}

void insertAfter(struct DoublyLinkedList *list, struct DoublyLinkedNode *node, struct DoublyLinkedNode *new_node) {
  new_node->prev = node;
  if (node->next == NULL) {
    new_node->next = NULL;
    list->tail = new_node;
  } else {
    new_node->next = node->next;
    node->next->prev = new_node;
  }
  node->next = new_node;
}

void insertBefore(struct DoublyLinkedList *list, struct DoublyLinkedNode *node, struct DoublyLinkedNode *new_node) {
  new_node->next = node;
  if (node->prev == NULL) {
    new_node->prev = NULL;
    list->head = new_node;
  } else {
    new_node->prev = node->prev;
    node->prev->next = new_node;
  }
  node->prev = new_node;
}

void insertHead(struct DoublyLinkedList *list, struct DoublyLinkedNode *new_node) {
  if (list->head == NULL) {
    list->head = new_node;
    list->tail = new_node;
    new_node->prev = NULL;
    new_node->next = NULL;
  } else {
    insertBefore(list, list->head, new_node);
  }
}

void insertTail(struct DoublyLinkedList *list, struct DoublyLinkedNode *new_node) {
  if (list->tail == NULL) {
    insertHead(list, new_node);
  } else {
    insertAfter(list, list->tail, new_node);
  }
}

void removeNode(struct DoublyLinkedList *list, struct DoublyLinkedNode *node) {
  if (node->prev == NULL) {
    list->head = node->next;
  } else {
    node->prev->next = node->next;
  }
  if (node->next == NULL) {
    list->tail = node->prev;
  } else {
    node->next->prev = node->prev;
  }
}

void printDoublyLinkedList(struct DoublyLinkedList *list) {
  struct DoublyLinkedNode *node = list->head;
  while (node != NULL) {
    printf("%s ", node->data);
    node = node->next;
  }
  printf("NULL\n");
}

int main() {
  printf("Creating list...\n");
  struct DoublyLinkedList *list = malloc(sizeof(struct DoublyLinkedList));
  list->head = NULL;
  list->tail = NULL;
  printDoublyLinkedList(list);
  struct DoublyLinkedNode *node1 = malloc(sizeof(struct DoublyLinkedNode));
  strcpy(node1->data, "Node1");
  insertHead(list, node1);
  printDoublyLinkedList(list);
  struct DoublyLinkedNode *node2 = malloc(sizeof(struct DoublyLinkedNode));
  strcpy(node2->data, "Node2");
  insertHead(list, node2);
  printDoublyLinkedList(list);
  struct DoublyLinkedNode *node3 = malloc(sizeof(struct DoublyLinkedNode));
  strcpy(node3->data, "Node3");
  insertTail(list, node3);
  printDoublyLinkedList(list);
  struct DoublyLinkedNode *node4 = malloc(sizeof(struct DoublyLinkedNode));
  strcpy(node4->data, "Node4");
  insertHead(list, node4);
  printDoublyLinkedList(list);
  return 0;
}