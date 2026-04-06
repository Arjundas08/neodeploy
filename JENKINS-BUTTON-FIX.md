# 🔧 JENKINS TRIGGER - Quick Setup Guide

## The Issue
Jenkins requires authentication to trigger builds from the dashboard.

## ✅ ONE-TIME FIX (Takes 30 seconds!)

### Step 1: Open Jenkins Security Settings
A browser tab should have opened to:
```
http://localhost:8081/manage/configureSecurity/
```

If not, open it manually.

### Step 2: Login to Jenkins (if needed)
Use your Jenkins username and password.

### Step 3: Change Authorization Setting
1. Scroll down to the **"Authorization"** section
2. You'll see: "Logged-in users can do anything"
3. **Change it to:** "Anyone can do anything"
   
   ![Authorization Setting](https://i.imgur.com/example.png)

### Step 4: Save
Click the **"Save"** button at the bottom of the page.

### Step 5: Test It!
1. Go back to: http://localhost:9090/index.html
2. Click **"Trigger Jenkins"** button
3. 🎉 It should work now!

---

## ⚠️ Security Note

"Anyone can do anything" is fine for:
- ✅ Local development
- ✅ Demo purposes
- ✅ Learning environments

For production, you'd use proper authentication.

---

## Alternative: Keep Security + Use API Token

If you want to keep Jenkins secure:

### Get Your API Token:
1. Open: http://localhost:8081/me/configure
2. Under "API Token", click "Add new Token"
3. Give it a name like "neodeploy"
4. Click "Generate"
5. **Copy the token** (you won't see it again!)

### Set Environment Variables:
```cmd
set JENKINS_USER=arjun
set JENKINS_TOKEN=your_token_here
```

### Restart the App:
```cmd
java -jar target/neodeploy-0.0.1-SNAPSHOT.jar --server.port=9090
```

---

## Quick Test Command

Test if Jenkins allows anonymous builds:
```powershell
curl -X POST http://localhost:8081/job/neodeploy-pipeline/build
```

- **Success:** No output (build started!)
- **403 Error:** Need to change security settings

---

## Done!

After following the steps above, your "Trigger Jenkins" button will work perfectly! 🚀
