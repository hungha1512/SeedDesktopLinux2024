# calculate.py
import sys

def main():
    # Check if arguments are passed
    if len(sys.argv) != 3:
        print("Usage: python3 calculate.py <num1> <num2>")
        return

    # Read numbers from command-line arguments
    try:
        num1 = float(sys.argv[1])
        num2 = float(sys.argv[2])
        result = num1 + num2
        print(f"Result is: {result}")
    except ValueError:
        print("Invalid numbers. Please enter valid numeric values.")

if __name__ == "__main__":
    main()
