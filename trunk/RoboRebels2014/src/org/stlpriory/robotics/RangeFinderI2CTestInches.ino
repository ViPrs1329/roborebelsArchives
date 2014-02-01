//
// FRC Team 1329 Roborebels
//
// Ultrasonic rangefinder using MaxBotix MB1000 transducer
//
// Communicates with cRIO over I2C bus in Digital Sidecar address 2
//
// Can be used with cRIOMasterReaderSimulatorInches sketch
//

#include <Wire.h>

int ms = 7;    // Using Pin 7 on Arduino Uno as PW output from MB1000

//initiallizes the variable x used later
int x = 0;

//declares the number array previous
int previous[] ={0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
int pin_a = 2;    // Led segment "a" is digital pin 0
int pin_b = 3;
int pin_c = 4;
int pin_d = 5;
int pin_e = 6;
int pin_f = 8;
int pin_g = 9;
int pin_dot = 10;

int distance = 0;  // rangefinder distance in inches (not averaged)

byte ch = 0;  // character for I2C test

byte feet;

void setup() {
  // put your setup code here, to run once:

  Serial.begin(9600);
  
  pinMode(ms, INPUT);
  pinMode(pin_a, OUTPUT);
  pinMode(pin_b, OUTPUT);
  pinMode(pin_c,OUTPUT);
  pinMode(pin_d,OUTPUT);
  pinMode(pin_e,OUTPUT);
  pinMode(pin_f,OUTPUT);
  pinMode(pin_g,OUTPUT);
  pinMode(pin_dot,OUTPUT);
  
  Wire.begin(2);                // join i2c bus with address #2
  Wire.onRequest(requestEvent); // register event when master requests data
}
  

void loop2() {

  //gets the main readings
 int pwm = pulseIn(ms, HIGH);
  
 distance = pwm / 147;
 
 //sets x to the current reading
 x = x + distance;
 
 //shifts the numbers in the array previous
 for(int i = 9; i>0; i--){
   previous[i] = previous[i-1];
 }
 //sets the first number in the array to the current reading
 previous[0] = distance;
 
 //this can print all the numbers in the array
 /* Serial.print("Array previous is ");
 
 for(int i = 0; i<10; i++){
   Serial.print(previous[i]);
   Serial.print(" ");
 }
 Serial.println(" "); */
 
 //makes sure x is 0 so no readings over 10 are recorded
 x = 0;
 
 //sets the array values to x for calculation
  for(int i = 0; i<10; i++){
    x = x + previous[i];
  }
  
  //calculates the moving average
    int distanceaverage = x / 10;
  
  //prints the moving average
  Serial.print("Moving average distance in inches is ");
  Serial.println(distanceaverage);
}


//this entire section can be omitted, it is used for calculating the non-moving average
void loop() {
  // put your main code here, to run repeatedly: 
 
 x = 0;
 
  for(int i = 0; i<10; i++){
    loop2();
  }
  
    int distanceaverage = x / 10;
  
  //prints the non-moving average
  Serial.print("Average distance in inches is ");
  Serial.println(distanceaverage);
  led_display(distanceaverage);
}
void led_display(int inches)
{
 feet = (inches + 6)/12;    // add 6 inches to round to nearest foot
 
 boolean a = false;
 boolean b = false;
 boolean c = false;
 boolean d = false;
 boolean e = false;
 boolean f = false;
 boolean g = false;
 boolean dot = false;
 
 switch(feet){
   
  case 0:
    // 0 feet
    a = true;
    b = true;
    c = true;
    d = true;
    e = true;
    f = true;
    g = false;
    dot = false;
    break;
    
    
    case 1:
    // 1 foot
    a = false;
    b = false;
    c = false;
    d = false;
    e = true;
    f = true;
    g = false;
    dot = false;
    break;
    
    
    case 2:
    // 2 feet
    a = true;
    b = true;
    c = false;
    d = true;
    e = true;
    f = false;
    g = true;
    dot = false;
    break;
    
    
    case 3:
    // 3 feet
    a = true;
    b = true;
    c = true;
    d = true;
    e = false;
    f = false;
    g = true;
    dot = false;
    break;
    
    
    case 4:
    //  feet
    a = false;
    b = true;
    c = true;
    d = false;
    e = false;
    f = true;
    g = true;
    dot = false;
    break;
    
    
    case 5:
    // 5 feet
    a = true;
    b = false;
    c = true;
    d = true;
    e = false;
    f = true;
    g = true;
    dot = false;
    break;
    
    
    case 6:
    // 6 feet
    a = true;
    b = false;
    c = true;
    d = true;
    e = true;
    f = true;
    g = true;
    dot = false;
    break;
    
    
    case 7:
    // 0 feet
    a = true;
    b = true;
    c = true;
    d = false;
    e = false;
    f = false;
    g = false;
    dot = false;
    break;
    
    
    case 8:
    // 8 feet
    a = true;
    b = true;
    c = true;
    d = true;
    e = true;
    f = true;
    g = true;
    dot = false;
    break;
    
    
    
    case 9:
    // 9 feet
    a = true;
    b = true;
    c = true;
    d = true;
    e = false;
    f = true;
    g = true;
    dot = false;
    break;
  
  default:
    // more than 9 shows as 9 with dot
    a = true;
    b = true;
    c = true;
    d = true;
    e = false;
    f = true;
    g = true;
    dot = true;
    break;
   
 
 }
  
  digitalWrite(pin_a, a);
  digitalWrite(pin_b, b);
  digitalWrite(pin_c, c);
  digitalWrite(pin_d, d);
  digitalWrite(pin_e, e);
  digitalWrite(pin_f, f);
  digitalWrite(pin_g, g);
  digitalWrite(pin_dot,dot);
  
}

void requestEvent()  // returns current distance in inches when requested by I2C bus master
{
 
  byte inch_byte = distance;
  
  Wire.write(inch_byte); // respond with message of single byte
                       // as expected by master
}

