#First set work dir!
setwd("/home/andreas/workspace/git/CPL1/NetworkSimulation")
################TURNBASED##################
flu<-read.table("fluspreadingData_turn.txt",header=TRUE)

# Calculate range from 0 to max value of data
y_range <- range(0, flu$Susceptible,flu$Infected,flu$Removed,flu$Nodes,flu$Av_Neighbours)
l = length((flu$Susceptible))
x_range <- range(0, l)
#summary of table
summary(flu)
#plot
plot(flu$Nodes,main="TurnBased",ylab="amount",xlab="sample",xlim=x_range,ylim=y_range,type="l")
lines(c(1:l),flu$Susceptible,col='green')
lines(c(1:l),flu$Infected,col='red')
lines(c(1:l),flu$Removed,col='blue')
legend(l-30,y_range[2]-10, c("TotalNodes","Susceptible","Infected","Removed"), cex=0.8, col=c("black","green","red","blue"), pch=21:22, lty=1:2);

################ROUNDBASED##################
flu<-read.table("fluspreadingData_round.txt",header=TRUE)

# Calculate range from 0 to max value of data
y_range <- range(0, flu$Susceptible,flu$Infected,flu$Removed,flu$Nodes,flu$Av_Neighbours)
l = length((flu$Susceptible))
x_range <- range(0, l)
#summary of table
summary(flu)
#plot
plot(flu$Nodes,main="TurnBased",ylab="amount",xlab="sample",xlim=x_range,ylim=y_range,type="l")
lines(c(1:l),flu$Susceptible,col='green')
lines(c(1:l),flu$Infected,col='red')
lines(c(1:l),flu$Removed,col='blue')
legend(l-30,y_range[2]-10, c("TotalNodes","Susceptible","Infected","Removed"), cex=0.8, col=c("black","green","red","blue"), pch=21:22, lty=1:2);

################EVENTBASED##################
flu<-read.table("fluspreadingData_event.txt",header=TRUE)

# Calculate range from 0 to max value of data
y_range <- range(0, flu$Susceptible,flu$Infected,flu$Removed,flu$Nodes,flu$Av_Neighbours)
l = length((flu$Susceptible))
x_range <- range(0, l)
#summary of table
summary(flu)
#plot
plot(flu$Nodes,main="TurnBased",ylab="amount",xlab="sample",xlim=x_range,ylim=y_range,type="l")
lines(c(1:l),flu$Susceptible,col='green')
lines(c(1:l),flu$Infected,col='red')
lines(c(1:l),flu$Removed,col='blue')
legend(l-30,y_range[2]-10, c("TotalNodes","Susceptible","Infected","Removed"), cex=0.8, col=c("black","green","red","blue"), pch=21:22, lty=1:2);

