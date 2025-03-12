// main.js
// 토큰 검사를 페이지가 언로드될 때 처리
Kakao.init('b5d8b9f0f40b2d55397786378ae4b1a4');

window.addEventListener('beforeunload', () => {
    checkAuthAndRefresh();
});

// 토큰 검사 및 리프레시
function checkAuthAndRefresh() {
    const currentTime = Date.now();
    const accessToken = localStorage.getItem('accessToken');
    const refreshToken = localStorage.getItem('refreshToken');
    const accessTokenExpiryTime = localStorage.getItem('accessTokenExpiryTime');

    if (!accessToken || accessToken.trim() === '') {
        window.location.href = '/login?error=unauthorized';  // 로그인 화면으로 이동
    } else if (currentTime > accessTokenExpiryTime) {
        if (refreshToken) {
            api.refreshToken(accessToken, refreshToken).then(response => {
                console.log(response); // 서버 응답 확인
                const newAccessToken = response.accessToken;
                const newRefreshToken = response.refreshToken;
                const newExpiryTime = response.accessTokenExpiryTime;

                localStorage.setItem('accessToken', newAccessToken);
                localStorage.setItem('refreshToken', newRefreshToken);
                localStorage.setItem('accessTokenExpiryTime', newExpiryTime);
            }).catch(error => {
                console.error(error); // 오류 로그 확인
                window.location.href = '/login?error=unauthorized';  // 로그인 화면으로 이동
            });
        } else {
            window.location.href = '/login?error=unauthorized';  // 로그인 화면으로 이동
        }
    }
}

// api.js
const api = {
    refreshToken: (accessToken, refreshToken) => {
        return fetch('/api/auth/refresh', {
            method: 'POST',
            headers: {
                "Content-Type": 'application/json'
            },
            body: JSON.stringify(
                {
                    accessToken: `Bearer ${accessToken}`,
                    refreshToken: refreshToken
                }
            )
        })
            .then(response => response.json())
            .then(data => data);  // 새로 발급된 토큰들 반환
    },

    fetchRestaurants: async (accessToken, params) => {
        const url = `/customer/api/restaurants?${params.toString()}`;
        try {
            const response = await fetch(url, {
                method: "GET",
                headers: {
                    "Authorization": `Bearer ${accessToken}`,
                    "Content-Type": "application/json"
                }
            });

            if (!response.ok) {
                throw new Error("가게 목록을 가져오는 데 실패했습니다.");
            }

            return await response.json();
        } catch (error) {
            console.error(error);
            throw error;
        }
    }
};

// event.js

let currentPage = 0; // 현재 페이지 번호
let isLoading = false; // 요청 중인지 여부
let hasMoreData = true; // 더 가져올 데이터가 있는지 여부

function goToStore(storeId) {
    window.location.href = '/restaurant?id=' + storeId;
}

async function searchStores(reset = true) {
    checkAuthAndRefresh();

    if (reset) {
        currentPage = 0;
        hasMoreData = true;
        document.getElementById("store-list").innerHTML = ""; // 기존 리스트 초기화
    }

    if (isLoading || !hasMoreData) return;
    isLoading = true;

    const keyword = document.getElementById("search-input").value;
    const address = document.getElementById("address").value;
    const minPrice = document.getElementById("min-price").value;
    const maxPrice = document.getElementById("max-price").value;
    const seatType = document.getElementById("seat-type").value;
    const isUseDistance = document.getElementById("nearby-btn").classList.contains("active");
    const order = document.querySelector(".sort-option.selected")?.dataset.sort || "popularity";

    let clientLat = null;
    let clientLon = null;

    if (isUseDistance && "geolocation" in navigator) {
        navigator.geolocation.getCurrentPosition((position) => {
            clientLat = position.coords.latitude;
            clientLon = position.coords.longitude;
            fetchRestaurants(keyword, address, minPrice, maxPrice, seatType, isUseDistance, clientLat, clientLon, order);
        }, (error) => {
            console.error("위치 정보를 가져오는 데 실패했습니다.", error);
            fetchRestaurants(keyword, address, minPrice, maxPrice, seatType, isUseDistance, clientLat, clientLon, order);
        });
    } else {
        fetchRestaurants(keyword, address, minPrice, maxPrice, seatType, isUseDistance, clientLat, clientLon, order);
    }
}

async function fetchRestaurants(keyword, address, minPrice, maxPrice, seatType, isUseDistance, clientLat, clientLon, order) {
    const params = new URLSearchParams();

    if (keyword !== undefined && keyword !== null) params.append("keyword", keyword);
    if (address !== undefined && address !== null) params.append("address", address);
    if (minPrice !== undefined && minPrice !== null) params.append("minPrice", minPrice);
    if (maxPrice !== undefined && maxPrice !== null) params.append("maxPrice", maxPrice);
    if (seatType !== undefined && seatType !== null) params.append("seatTypeId", seatType);
    if (isUseDistance !== undefined && isUseDistance !== null) params.append("isUseDistance", isUseDistance ? "true" : "false");
    if (clientLat !== undefined && clientLat !== null) params.append("clientLat", clientLat);
    if (clientLon !== undefined && clientLon !== null) params.append("clientLon", clientLon);
    if (order !== undefined && order !== null) params.append("order", order);

    params.append("page", currentPage);

    const accessToken = localStorage.getItem('accessToken');

    try {
        const data = await api.fetchRestaurants(accessToken, params);

        if (data.content.length === 0) {
            hasMoreData = false; // 더 이상 가져올 데이터 없음
        } else {
            displayRestaurants(data.content);
            currentPage++; // 페이지 번호 증가
        }
    } catch (error) {
        console.error(error);
    } finally {
        isLoading = false;
    }
}

function displayRestaurants(restaurants) {
    const storeList = document.getElementById("store-list");

    console.log(restaurants);
    console.log("들어옴");
    restaurants.forEach(restaurant => {
        const storeItem = document.createElement("div");
        storeItem.classList.add('store-item');
        storeItem.innerHTML = `
                <div class="store-header">
                    <div class="store-name">
                        <h2>${restaurant.name}</h2>
                        <button class="bookmark-button ${restaurant.isBookmark ? 'active' : ''}"
                                onclick="toggleBookmark('${restaurant.id}', this)">
                            <i class="fas fa-bookmark"></i>
                        </button>
                    </div>
                </div>
                <div class="store-address">
                    <p>${restaurant.address}</p>
                </div>
                <div class="store-info">
                    <p>${restaurant.openTime} - ${restaurant.closeTime} | 가격: ${restaurant.minPrice} ~ ${restaurant.maxPrice}원</p>
                </div>
            `;

        storeList.appendChild(storeItem);

    });
}

// 무한 스크롤 이벤트 추가
let debounceTimeout;
window.addEventListener("scroll", () => {
    clearTimeout(debounceTimeout);
    debounceTimeout = setTimeout(() => {
        if (window.innerHeight + window.scrollY >= document.body.offsetHeight - 500) {
            searchStores(false); // 추가 데이터 로드
        }
    }, 200);  // 200ms마다 호출
});

// 필터 검색
function openSortModal() {
    document.getElementById("sort-modal").style.display = "flex";
}

function closeSortModal() {
    document.getElementById("sort-modal").style.display = "none";
}

function selectSort(event) {
    const selectedOption = event.currentTarget;
    if (!selectedOption) return;

    const sortValue = selectedOption.dataset.sort;
    console.log("선택된 정렬 기준:", sortValue);

    document.querySelectorAll('.sort-option').forEach(option => {
        option.classList.remove('selected');
    });

    selectedOption.classList.add('selected');
}

document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".sort-option").forEach(option => {
        option.addEventListener("click", selectSort);
    });
});

function openFilterModal() {
    document.getElementById("filter-modal").style.display = "flex";
}

function closeFilterModal() {
    document.getElementById("filter-modal").style.display = "none";
}

function applyFilters() {
    closeFilterModal();
}