# Image 7Niu Loader

## I. 使用

```
ImageLoader.display7Niu(image1, MOCK_DATA_URL)
                .attach();

ImageLoader.display7Niu(image2, MOCK_DATA_URL)
                .centerCrop()
                .wR(R.dimen.image_2_width)
                .attach();

ImageLoader.display7Niu(image3, MOCK_DATA_URL)
                .size(dp2px(250))
                .addOpBlur(40, 20)
                .attach();

ImageLoader.display7Niu(image4, MOCK_DATA_URL)
                .w(dp2px(270))
                .addOpRotate(30)
                .attach();

ImageLoader.display7Niu(image5, MOCK_DATA_URL)
                .maxHalfW()
                .attach();
```


## II. LICENSE

```
Copyright (c) 2015 LingoChamp Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
