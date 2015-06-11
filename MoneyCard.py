import subprocess
import sys

def getCommands(command):
  lines = ""
  output = ""
  # print command
  solver = subprocess.Popen(
      command, stdin=subprocess.PIPE, stdout=subprocess.PIPE)
  
  output += solver.communicate(lines)[0]
  while solver.poll():
		output += solver.communicate()[0]
  # print output
  return output

# print 'Hello!'
ret = getCommands(["sudo", "./test", sys.argv[1]])
ret = ret.split('\n')

moneystr = ret[-2].split(':')[1].replace(" ", "")

if len(moneystr) < 8:
  print 'fail!z'
else:
  print "%s%s" % (moneystr[2:-4], 'z')

