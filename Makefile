# Usage:
# make				# compile all binary
# make clean	# remove ALL binaries and objects

CC = gcc
CFLAGS = -Wall -Wextra -Wno-unused-parameter -Wno-unused-function -O0 -DDEBUG -g -std=c99

NAME := clox
SOURCE_DIR := clox
BUILD_DIR := build

HEADERS := $(wildcard $(SOURCE_DIR)/*.h)
SOURCES := $(wildcard $(SOURCE_DIR)/*.c)
OBJECTS := $(addprefix $(BUILD_DIR)/$(NAME)/, $(notdir $(SOURCES:.c=.o)))

build/$(NAME): $(OBJECTS)
	@ $(CC) $(CFLAGS) $^ -o $@

$(BUILD_DIR)/$(NAME)/%.o: $(SOURCE_DIR)/%.c $(HEADERS)
	@ $(CC) -c $(CFLAGS) -o $@ $<

.PHONY: default