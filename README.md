ImageEditor
============

![Logo](/pic/logo.png)


It looks like what ?
--------------

![Editor](/pic/editor01.gif)


How to use lib
--------------

```groovy
dependencies {
  compile 'com.github.lauhwong:image-editor:1.0'
}
```
and then use it in your application like this:

```kotlin
val setup = EditorSetup(source, mOriginalPath, getEditorSavePath())
val intent = ImageEditorActivity.intent(this, setup)
startActivityForResult(intent, ACTION_REQUEST_EDITOR)
```
receive iamgeEditor result from onActivityResult may be like code below:

```kotlin
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
	super.onActivityResult(requestCode, resultCode, data)
        data ?: return
        if (resultCode == Activity.RESULT_OK) {
		when (requestCode) {
			ACTION_REQUEST_EDITOR -> {
                    	val result = data.getSerializableExtra(resultCode.toString()) as EditorResult
			handleResultFromEditor(result)
			}
		}
	}
}

```
more *details usage* can be explore in *example module*

License
-------

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    
 Reference
 ----------
 1.<https://github.com/chrisbanes/PhotoView>  
 2.<https://github.com/Curzibn/Luban>
