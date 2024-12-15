package com.example.danum.feature.presentation.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.danum.core.data.model.Store;
import com.example.danum.R;
import com.example.danum.feature.presentation.my.MyInfoActivity;
import com.example.danum.feature.presentation.product.ProductRegistrationActivity;
import com.example.danum.feature.presentation.product.ProductDetailActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.kakao.vectormap.GestureType;
import com.kakao.vectormap.KakaoMap;
import com.kakao.vectormap.KakaoMapReadyCallback;
import com.kakao.vectormap.MapLifeCycleCallback;
import com.kakao.vectormap.MapView;
import com.kakao.vectormap.LatLng;
import com.kakao.vectormap.camera.CameraPosition;
import com.kakao.vectormap.camera.CameraUpdateFactory;
import com.kakao.vectormap.label.Label;
import com.kakao.vectormap.label.LabelLayer;
import com.kakao.vectormap.label.LabelOptions;
import com.kakao.vectormap.label.LabelStyle;
import com.kakao.vectormap.label.LabelStyles;
import com.kakao.vectormap.label.LabelTextBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private ImageView userImageView,
            fruitImageView, vegetableImageView, meatImageView, bakeryImageView,
            filterTransactionImageView, filterSharingImageView, filterTransactionAndSharingImageView,
            addAndModifyImageView;
    private MapView mapView;
    private KakaoMap kakaoMap;
    private Label locationLabel;
    private List<Label> storeLabels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initializeUIElements();
        initializeKakaoMap();
        initializeTransparentRecyclerView();
        getLocation();
    }

    private void initializeKakaoMap() {
        mapView = findViewById(R.id.map_view);
        mapView.start(new MapLifeCycleCallback() {
            @Override
            public void onMapDestroy() {
                Log.d("카카오맵 api", "카카오맵 api onMapDestroy");
            }

            @Override
            public void onMapError(Exception error) {
                Log.e("카카오맵 api", "카카오맵 api onMapError", error);
            }
        }, new KakaoMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull KakaoMap map) {
                Log.d("카카오맵 api", "카카오맵 api onMapReady");
                kakaoMap = map;
                setupMap();
            }
        });
    }

    private void setupMap() {
        if (checkLocationPermission()) {
            moveToCurrentLocation();
        } else {
            requestLocationPermission();
        }
        kakaoMap.getZoomLevel();
        kakaoMap.setGestureEnable(GestureType.Pan, true);
        kakaoMap.setGestureEnable(GestureType.OneFingerDoubleTap, true);

        // 상점 마커 추가
        addStoreMarkers();

        // 마커 클릭 리스너 설정
        kakaoMap.setOnLabelClickListener(new KakaoMap.OnLabelClickListener() {
            @Override
            public boolean onLabelClicked(@NonNull KakaoMap kakaoMap, @NonNull LabelLayer layer, @NonNull Label clickedLabel) {
                String[] textsArray = clickedLabel.getTexts();
                if (textsArray != null && textsArray.length >= 2) {
                    List<String> texts = Arrays.asList(textsArray);
                    String storeName = texts.get(0);
                    String storeAddress = texts.get(1);
                    showStoreDetailDialog(storeName, storeAddress);
                }
                return false;
            }
        });
    }

    private void addStoreMarkers() {
        List<Store> storeList = getStores();  // 스토어 목록을 가져옴
        for (Store store : storeList) {

            // 각 상점의 위치 (위도, 경도) 가져오기
            LatLng position = LatLng.from(store.getLatitude(), store.getLongitude());

            // 마커에 표시할 텍스트 구성
            LabelTextBuilder textBuilder = new LabelTextBuilder();
            textBuilder.setTexts(store.getName());  // 상점 이름
            textBuilder.setTexts(store.getAddress());   // 상점 주소

            // 마커 이미지 아이콘 준비 (Drawable을 Bitmap으로 변환)
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_transaction_marker);
            if (drawable == null) {
                Log.e("마커 이미지", "Drawable 리소스를 찾을 수 없습니다.");
                return;
            }

            // Drawable을 Bitmap으로 변환
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);

            // 마커 이미지 크기 조정 (예: 40x40 크기)
            int width = 40;
            int height = 40;
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);

            // LabelStyle 객체로 변환
            LabelStyle style = LabelStyle.from(scaledBitmap);

            // 마커에 텍스트 및 스타일 적용
            LabelOptions options = LabelOptions.from(position)
                    .setStyles(LabelStyles.from(style))  // 스타일 (아이콘 및 크기)
                    .setTexts(textBuilder);              // 텍스트 (상점명 및 주소)

            // 지도에 마커 추가
            LabelLayer labelLayer = kakaoMap.getLabelManager().getLayer();
            Label label = labelLayer.addLabel(options);

            // 마커가 성공적으로 추가되면 로그 출력
            if (label != null) {
                storeLabels.add(label);
                Log.d("마커 추가", "상점 마커가 추가되었습니다: " + store.getName());
            }
        }
    }

    private void showStoreDetailDialog(String name, String address) {
        // 상점 상세 정보를 표시하는 다이얼로그 구현
        //Toast.makeText(this, "상점: " + name + "\n주소: " + address, Toast.LENGTH_LONG).show();
    }

    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    private void moveToCurrentLocation() {
        if (kakaoMap == null) return;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = null;
        if (locationManager != null) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        }

        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            // 위치 정보 로그 출력
            Log.d("현재 위치", "Latitude: " + latitude + ", Longitude: " + longitude);

            LatLng myLocation = LatLng.from(latitude, longitude);
            CameraPosition cameraPosition = CameraPosition.from(
                    myLocation.latitude,
                    myLocation.longitude,
                    6, // 줌 레벨
                    0.0, // 기울기
                    0.0, // 회전
                    0.0  // 경사
            );
            kakaoMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            addMyLocationMarker(myLocation);
        } else {
            Toast.makeText(this, "현재 위치를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }


    private void addMyLocationMarker(LatLng location) {
        if (locationLabel != null) {
            locationLabel.remove();
        }

        // Drawable을 Bitmap으로 변환
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_my_location);
        if (drawable == null) {
            Log.e("내 위치 마커", "Drawable 리소스를 찾을 수 없습니다.");
            return;
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        // Bitmap 크기 조절 (예: 50% 축소)
        int width = 40;
        int height = 40;
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);

        // LabelStyle에 크기 조절된 Bitmap 사용 (SDK가 Bitmap을 직접 지원하는지 확인 필요)
        LabelStyle style = LabelStyle.from(scaledBitmap); // SDK가 Bitmap을 지원해야 함
        LabelTextBuilder textBuilder = new LabelTextBuilder();
        textBuilder.setTexts("내 위치");

        LabelOptions options = LabelOptions.from(location)
                .setStyles(LabelStyles.from(style))
                .setTexts(textBuilder);

        LabelLayer labelLayer = kakaoMap.getLabelManager().getLayer();
        Label label = labelLayer.addLabel(options);
        if (label != null) {
            locationLabel = label;
            Log.d("내 위치 마커", "내 위치 마커가 추가되었습니다.");
        }
    }

    private void initializeUIElements() {
        userImageView = findViewById(R.id.userImageView);
        userImageView.setOnClickListener(v -> {
            // TODO: 사용자 정보 화면으로 이동
            Intent intent = new Intent(this, MyInfoActivity.class);
            startActivity(intent);

        });

        fruitImageView = findViewById(R.id.fruitImageView);
        fruitImageView.setOnClickListener(v -> {
            // TODO: 과일 필터 적용

        });

        vegetableImageView = findViewById(R.id.vegetableImageView);
        vegetableImageView.setOnClickListener(v -> {
            // TODO: 야채 필터 적용
        });

        meatImageView = findViewById(R.id.meatImageView);
        meatImageView.setOnClickListener(v -> {
            // TODO: 육류 필터 적용
        });

        bakeryImageView = findViewById(R.id.bakeryImageView);
        bakeryImageView.setOnClickListener(v -> {
            // TODO: 빵집 필터 적용

        });

        filterTransactionImageView = findViewById(R.id.filterTransactionImageView);
        /*filterSharingImageView.setOnClickListener(v -> {
            // TODO: 거래 필터 적용
        });*/

        filterSharingImageView = findViewById(R.id.filterSharingImageView);
        filterSharingImageView.setOnClickListener(v -> {
            // TODO: 나눔 필터 적용
        });

        filterTransactionAndSharingImageView = findViewById(R.id.filterTransactionAndSharingImageView);
        filterTransactionAndSharingImageView.setOnClickListener(v -> {
            // TODO: 거래/나눔 필터 적용
        });

        addAndModifyImageView = findViewById(R.id.addAndModifyImageView);
        addAndModifyImageView.setOnClickListener(v -> {
            // 상품 추가 및 수정 화면으로 이동
            Intent intent = new Intent(this, ProductRegistrationActivity.class);
            startActivity(intent);
        });
    }

    public static List<Store> getStores() {
        List<Store> storeList = new ArrayList<>();
        storeList.add(new Store(1, "과일숲", "경기도 용인시 수지구 고기동 345-67", 37.352, 127.072,
                0, "https://danumbucket.s3.ap-northeast-2.amazonaws.com/store-images/%E1%84%80%E1%85%AA%E1%84%8B%E1%85%B5%E1%86%AF.jpeg"
                ));

        storeList.add(new Store(2, "수지 이마트", "경기도 용인시 수지구 고기동 890-12", 37.3505, 127.071,
                1,"https://danumbucket.s3.ap-northeast-2.amazonaws.com/store-images/%E1%84%8B%E1%85%B5%E1%84%86%E1%85%A1%E1%87%80%E1%84%90%E1%85%B3.jpeg"
             ));

        storeList.add(new Store(3, "파리바게뜨", "경기도 용인시 수지구 고기동 211-45", 37.3517, 127.072,
                2, "https://danumbucket.s3.ap-northeast-2.amazonaws.com/store-images/%E1%84%88%E1%85%A1%E1%86%BC%E1%84%8C%E1%85%B5%E1%86%B8.jpeg"
               ));

        storeList.add(new Store(4, "바다 품은 식당마켓", "경기도 용인시 수지구 고기동 123-78", 37.351, 127.071,
                2,"https://danumbucket.s3.ap-northeast-2.amazonaws.com/store-images/%E1%84%89%E1%85%A2%E1%86%BC%E1%84%89%E1%85%A5%E1%86%AB%E1%84%80%E1%85%A1%E1%84%80%E1%85%A6.jpeg"
                ));

        storeList.add(new Store(5, "초록마을", "경기도 용인시 수지구 고기동 102-54", 37.3508, 127.072,
                0,"https://danumbucket.s3.ap-northeast-2.amazonaws.com/store-images/%E1%84%8B%E1%85%B2%E1%84%80%E1%85%B5%E1%84%82%E1%85%A9%E1%86%BC+%E1%84%80%E1%85%A1%E1%84%80%E1%85%A6.jpeg"
               ));
        return storeList;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == LOCATION_PERMISSION_REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                moveToCurrentLocation();
            } else {
                Toast.makeText(this, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mapView != null){
            mapView.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mapView != null){
            mapView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mapView != null){
            mapView.finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    /**
     * 위치정보 권한이 이미 허용되었다고 가정하고 위치 정보를 가져오는 메서드
     *
     */
    @SuppressLint("MissingPermission")
    private void getLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            getAddress(location.getLatitude(), location.getLongitude());
                            Log.d("위치 정보", "위도: " + location.getLatitude() + ", 경도: " + location.getLongitude());
                        } else {
                            Toast.makeText(HomeActivity.this, "위치 정보를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
                            Log.d("위치 정보", "위치 정보를 가져올 수 없습니다.");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HomeActivity.this, "위치 정보를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
                        Log.e("위치 정보", "위치 정보를 가져오는 데 실패했습니다.", e);
                    }
                });

    }

    private List<Address> getAddress(double lat, double lng) {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.KOREA);
            List<Address> addressList = geocoder.getFromLocation(lat, lng, 1);
            Log.d("주소", "위치 주소: " + addressList.get(0).getAddressLine(0));
            Toast.makeText(this, "주소: " + addressList.get(0).getAddressLine(0), Toast.LENGTH_SHORT).show();
            TextView districtsTextView = findViewById(R.id.districtsTextView);
            districtsTextView.setText(extractFourthWord(addressList.get(0).getAddressLine(0)));
            return addressList;
        } catch (IOException e) {
            Toast.makeText(this, "주소를 가져 올 수 없습니다", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    /**
     * 주어진 문자열을 공백으로 분리하여 네 번째 단어를 반환합니다.
     *
     * @param address 전체 주소 문자열
     * @return 네 번째 단어 또는 빈 문자열
     */
    private String extractFourthWord(String address) {
        if (address == null || address.isEmpty()) {
            return "";
        }

        String[] parts = address.split(" ");
        if (parts.length >= 4) {
            return parts[3]; // 인덱스는 0부터 시작하므로 네 번째 단어는 인덱스 3
        } else {
            return "";
        }
    }

    private void initializeTransparentRecyclerView() {
        RecyclerView transactionRecyclerView = findViewById(R.id.transactionRecyclerView);
        transactionRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Store> storeList = getStores();
        StoreAdapter adapter = new StoreAdapter(this, storeList, new StoreAdapter.OnItemClickListener() {
            @Override
            public void onActionClick(Store store) {
                Log.d("HomeActivity", "Action Button Clicked. Store ID: " + store.getId() + ", Name: " + store.getName());
                // 버튼 클릭 시 동작 (예: 거래/나눔 필터 적용 등)
                Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
                intent.putExtra("STORE_ID", store.getId());
                intent.putExtra("STORE_NAME", store.getName());
                intent.putExtra("STORE_ADDRESS", store.getAddress());
                startActivity(intent);
            }

            @Override
            public void onItemClick(Store store) {
                Log.d("HomeActivity", "Item Clicked. Store ID: " + store.getId() + ", Name: " + store.getName());
                // 전체 아이템 클릭 시 상세 페이지로 이동
                Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
                intent.putExtra("STORE_ID", store.getId());
                intent.putExtra("STORE_NAME", store.getName());
                intent.putExtra("STORE_ADDRESS", store.getAddress());
                startActivity(intent);
            }
        });
        transactionRecyclerView.setAdapter(adapter);
    }

}
