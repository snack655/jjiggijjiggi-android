package kr.minjae.develop.jjiggijjiggi.feature.main.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_camera.iv_image
import kr.minjae.develop.jjiggijjiggi.R
import kr.minjae.develop.jjiggijjiggi.databinding.ActivityMainBinding
import kr.minjae.develop.jjiggijjiggi.feature.camera.view.CameraActivity
import kr.minjae.develop.jjiggijjiggi.feature.main.adapter.SolveAdapter
import kr.minjae.develop.jjiggijjiggi.feature.main.data.SolveData
import kr.minjae.develop.jjiggijjiggi.feature.main.viewmodel.MainViewModel
import kr.minjae.develop.jjiggijjiggi.network.response.ocr.OcrResponse

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var url: String
    private lateinit var solveAdapter: SolveAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActivity()
        bindViewModel()
        bindViewEvent()
        initSolveAdapter()

        url = intent.getStringExtra(CameraActivity.INTENT_IMAGE_NAME) ?: return
        mainViewModel.getTextInImage(url)
    }

    private fun bindViewModel() = lifecycleScope.launchWhenStarted {
        mainViewModel.ocrState.collect { state ->
            if (state.ocrData != null) {
                setOcrData(state.ocrData)
                binding.scrollView.visibility = View.VISIBLE
                binding.progressCircular.visibility = View.GONE
                binding.layoutAppBar.visibility = View.VISIBLE
            }

            if (state.isLoading) {
                binding.progressCircular.visibility = View.VISIBLE
                binding.scrollView.visibility = View.GONE
                binding.layoutAppBar.visibility = View.GONE
            }

            if (state.error.isNotBlank()) {
                Toast.makeText(this@MainActivity, state.error, Toast.LENGTH_SHORT).show()
                binding.scrollView.visibility = View.VISIBLE
                binding.progressCircular.visibility = View.GONE
                binding.layoutAppBar.visibility = View.VISIBLE
            }
        }
    }

    private fun initActivity() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    private fun setOcrData(ocrData: OcrResponse) {
        val fieldList = ocrData.images[0].fields
        var str = ""
        fieldList.forEach {
            str += " " + it.inferText
        }
        binding.tvText.text = str

        Glide.with(binding.ivImage.context)
            .load(url)
            .error(R.drawable.dummy)
            .into(iv_image)

        setSolveData(str)
    }

    private fun setSolveData(originalText: String) {
        //solveAdapter.submitList(solveContents)

        val solveNewList: MutableList<SolveData> = mutableListOf()
        solveContents.forEach { solveData ->
            if (originalText.contains(solveData.title)) {
                solveNewList.add(solveData)
            }
        }
        solveAdapter.submitList(solveNewList)
    }

    private fun initSolveAdapter() {
        solveAdapter = SolveAdapter()
        binding.rvSolveContents.adapter = solveAdapter
    }

    private fun bindViewEvent() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private val solveContents: List<SolveData> = listOf(
        SolveData("전통적인 오행설", "오행은 방향(동⋅서⋅남⋅북⋅중앙)과 계절(봄⋅여름⋅가을⋅겨울⋅환절기), 물질(나무⋅쇠⋅불⋅물⋅흙), 색깔(청⋅백⋅적⋅흑⋅황), 동물(청룔⋅백호⋅주작⋅현무⋅황룡)등으로 나타낼 수 있다."),
        SolveData("1968년 재건된 광화문", "한국전쟁 때 폭탄에 맞아 부서진 걸 18년 만에 복원한 것 이다."),
        SolveData("서십자각은 일제강점기에 철거", "일제가 전찻길을 내면서 서십자각을 없애버렸다."),
        SolveData("북악산을 주산으로, 목멱산(지금의 남산)을 안산으로 삼아", "사람이 살기 좋은 곳을 찾는 우리 고유의 학문을 '풍수지리'라고 한다. 풍수지리에서 말하는 명당, 즉'좋은곳'의 뒤에는 주산이, 앞에는 안산이 있어야 한다고 해. 주산은 뒤에서 받쳐주는 산, 안산은 앞에서 막아주는 산을 말하지"),
        SolveData("관청가인 육조 거리(지금의 세종로)를 조성하고", "지금은 광화문 광장이 들어선 육조 거리에 조선시대에는 이조,호조,예조,병조,형조,공조의 6조뿐 아니라 의정부, 사헌부 등 중요 관청들이 몰려있었어. 조선 최고의 정치, 행정 중심지였지"),
        SolveData("조선의 상징 축", "조선총독부가 비뚜름히 들어서면서 이 중심축이 무너졌다"),
        SolveData("1867년에야 비로소 흥선대원군이 허약해진 왕권을 강화하기 위해 경복궁을 재건했다", "고종의 아버지인 흥선대원군은 어린 고종 대신 나라를 다스렸어. 그전까지 몇몇 힘 있는 가문이 나라를 좌우하던 세도정치가 이루어졌는데, 이를 없애고 왕권을 강화했지. 그걸 위해 경복궁도 다시 지은 거란다."),
        SolveData("약 500여 동의 건물", "이 중 지금까지 남아 있거나 복원된 건물은 약 4분의 1에 불과해"),
        SolveData("일제강점기에 대부분의 건물들이 철거돼 중심부의 일부 건물들만 남았고", "일제강점기에도 살아남은 건물은 근정전과 사정전, 수정전, 경화루, 향원정 등이야"),
        SolveData("정전", "'정전'이란 궁궐의 으뜸 건물로 나라의 큰 행사를 치르는 곳 이야. 궁궐의 건물은 용도에 따라 달리 부르는데, 신하들과 국정을 논하는 곳은 편전, 국왕의 생활 공간은 대전, 왕비의 거처는 중전이라고 해. 그래서 왕비를 '중전마마'라 하는 거야. 궁궐마다 정전, 편전 등이 모두 있는데, 경복궁의 정전은 근정전, 편전은 사정전, 대전은 강녕전, 중전은 교태전이란다."),
        SolveData("행각", "지금 좌우로 보이는 것처럼 벽 없이 기둥만 선 복도를 '회랑'이라 하고, 이 회랑을 벽으로 막아서 사무실이나 창고로 쓰도록 한 것을 행각이라고 해. 지금은 모두 회랑이 됐지만, 기둥 아래를 보면 나무를 끼워 벽을 세운 흔적을 찾을 수 있지."),
        SolveData("품계석", "조선시대에는 관직의 등급을 '품계'라 불렀어. 정1품에서 종9품까지 모두 18등급이 있었단다. 조회를 할 때 관리들은 자신의 직급에 맞는 품계석에 줄지어 섰지."),
        SolveData("박석", "옛날 마당에 깔았던 돌을 박석이라고 해. 궁궐의 박석이 울퉁불퉁한 것은 돌을 다듬는 기술이 모자라서가 아니라, 맑은 날 햇빛의 반사를 줄이고, 비 오는 날 미끄러지는 것을 막기위해서래. 궁궐 밖에도 땅이 질거나 보호해야 할 곳에 이 돌을 깔기도 했는데, 그런 곳들은 지금도'박석고개'라 불리곤 하지."),
        SolveData("금천", "아까 말했던 푸웃지리에서는 주산과 안산 말고도 명당 앞에는 반드시 물이 흘러야 한다고 믿었어. 그래서 궁궐마다 일부러 개천을 흐르게 하고 '금천'이라 불렀지. 당연히 금천은 경복궁뿐 아니라 다른 궁궐에도 모두 있단다. 금천교는 금천을 가로지르는 다리를 말해. 금천교를 지나면 지엄한 왕의 공간으로 들어가게 되는 거야."),
        SolveData("어도", "글자 그대로 '왕의 길'이야. 가운데가 볼록하게 나와 있어서 신하가 다니는 길과 구별되지. 왕은 주로 가마를 타고 어도 위를 다니셨대."),
        SolveData("동궁(東宮)", "그래서 사극을 보면 세자를 가리켜'동궁마마'라고 하는 거야. '중전마마'는 중전에서 살고 있는 왕비를 부르는 별칭이었고. 하지만 '상감마마'의 '상감'은 국왕이 사는 곳이 아니라 임금을 높여 부르는 말이란다. '가장 높은 곳에서 백성을 두루 살피는 분'이란 뜻이래. '마마'는 왕이나 왕족 뒤에 붙이는 존칭이고"),
        SolveData("외전", "궁궐 건물들은 기능에 따라 크게 외전과 내전으로 나눌 수 있어. 외전은 근정전이나 서정전처럼 국왕이 신하와 함께 정무를 보는 곳이고, 내전은 왕과 왕족의 사적인 생활 공간이야. 왕의 침전인 강녕전이나 왕비의 침소인 교태전 등을 내전이라고 부른단다."),
        SolveData("소주방(燒廚房)", "'익힐 소(燒)'에'부엌 주(廚)'자를 써서 '음식을 만드는 부엌', 즉 대궐의 주방을 가리킨다. '수라간'이라고도 했다"),
        SolveData("수라", "임금의 식사를 뜻하는 '수라'는 몽골에서 온 말이야. 고려가 몽골의 부마국이었을 때 들어왔지. 왕과 왕비를 높이는 호칭인 '마마', 하급 궁녀를 뜻하는 '무수리'도 그때 들어온 말이래."),
        SolveData("조선물산공진회(朝鮮物産共進會)", "일제의 식민 통치 5주년을 기념하는 행사야. 그동안 여러 산업이 발전했다는 걸 보여주기 위한 박람회였어. 이 행사를 경복궁에서 치르면서 많은 전각이 없어지거나 훼손됐단다."),
        SolveData("경연", "왕과 신하가 함께 학문을 공부하는 것을 경연이라고 하는데, 보통 조강, 주강, 석강, 이렇게 하루 세 번 했다고 해. 교과서는 유교 경전인 사서오경이나 역사서였어. 세자 때 엄마 배 속에서 시작된 공부가 왕이 돼서도 계속됐던 거야."),
        SolveData("온돌방", "추울 때는 만춘전이나 천추전에서 회의를 했지"),
        SolveData("창고", "왕실의 재물을 보관하는 창고를 '내탕고'라고 해"),
        SolveData("강녕전(康寧殿)", "'강녕'은 몸이 건강하고 마음이 편안한 생태를 이르는 말로 '오복(五福)'중 하나래."),
        SolveData("교태전(交泰殿)", "'교태'는 <주역>에 나오는 말로, '땅과 하늘의 기운이 화합한다'라는 멋진 뜻이야. 하늘은 왕, 땅은 왕비, 그러니 둘이 잘 산다는 말이겠지?"),
        SolveData("1917년에 강녕전과 교태전을 뜯어 창덕궁으로 옮겨", "1917년 당시 순종임금이 있던 창덕궁의 침전이 불에 타자 일제는 경복궁의 강녕전과 교태전을 뜯어 창덕국의 희정당과 대조전을 지었어. 경복궁을 없애버리기 위한 행동이었지"),
        SolveData("아미산", "아미산은 경회루 연못을 팔 때 나온 흙으로 만든 작은 인공 산이야."),
        SolveData("자경전", "자경(慈慶)이란 '왕실의 어른들에게 경사가 있기를 기원한다'는 뜻이야. 정조임금이 어머니 혜경궁 홍씨를 위해 창경궁에 지은 '자경당'에서 유래한 말이래"),
        SolveData("조 대비", "대비는 임금의 어머니를 높여 부르는 말이지"),
        SolveData("누마루", "'누(樓)'라는 것은 땅에서 사람 키 높이 이상 띄워 지은 건물을 말해. '경회루'가 대표적이지. 마루 중에서도 이렇게 아래 기둥을 세우고 공중에 띄워 설치한 높은 마루를 '누마루'라고 해"),
        SolveData("협경당", "여기에는 주로 대비를 모시는 상궁이나 나인들이 묵었대"),
        SolveData("향원정", "향원정이란 이름은 '향기는 멀리 갈수록 맑아진다'라는 뜻이래."),
        SolveData("흥선대원군의 간섭에서 정치적으로 자립하기 위해 1873년", "이때 고종의 나이는 만 스물하나. 열한 살이라는 어린 나이에 즉위했기에 10년간은 아버지가 대신 나라를 다스렸어. 하지만 이제 고종이 직접 나라를 다스릴 나이가 된 거지. 건청궁을 지은 해 11월에 흥선대원군은 운현궁으로 물러나고 고종이 왕권을 되찾게 됐단다."),
        SolveData("시해", "부모나 임금을 죽이는 일을 이렇게 불러"),
        SolveData("1876년 경복궁에 큰 불", "고종 때 경복궁엔 유난히 화재가 잦았어. 경복궁을 다시 짓는 중간에도, 다 짓고 난 뒤에도 몇 번이나 큰 불이 났다니까. 그중에서도 1876년에 가장 피해가 심했다는구나"),
        SolveData("서재", "고종의 서재엔 책이 무려 4만 권이나 있었대! 이 정도면 '왕실 도서관'이라 부를 만하군. 여기서 외국 사신을 접견했다니 편전 역할을 했다고 볼 수도 있어."),
        SolveData("중2층", "북쪽 일부를 복층, 나머지는 단층으로 만든 구조야"),
        SolveData("중국풍의 요소", "자, 그럼 건물 가까이 가서 '중국풍' 요소들을 하나하나 찾아보자"),
        SolveData("빈전(殯殿)", "왕이나 왕비가 세상을 떠나면 그 시신을 장례 때까지 보관하는 곳이야. 보통 사람들의 '빈소'에 해당하는 곳이지."),
        SolveData("산릉", "아직 이름이 정해지지 않은 왕릉을 가리켜. 조선시대에 왕릉을 만드는 임시 관청 이름도 '산릉도감'이었어"),
        SolveData("혼전(魂殿)", "국장을 치르고 나서 신주(죽은 사람의 이름을 적어 넣은 나뭇조각)를 모시는 곳이야. 여기서 3년 동안 모시고 난 뒤에 종묘로 옮기게 된데"),
        SolveData("신위", "신주를 모셔두는 자리를 말해"),
        SolveData("경회루", "'경회(慶會)'는 '임금과 신하가 덕으로써 만난다'는 뜻이래."),
        SolveData("24절기", "태양의 움직임에 따라 1년을 24로 나눈 날들이야. 절기에 따라 계절의 변화를 알 수 있어서 농사짓는 데 도움이 됐지. '입춘', '우수', '경칩'같은 한자 이름이 좀 어려워 보이는데, 뜻은 '봄이 오는 날', '첫 봄비 오는 날', '개구리 깨어나는 날'처럼 간단해."),
        SolveData("청동 용", "경회루 옆에 드므를 만든 이유랑 같아. 불을 막기 위해서지"),
        SolveData("궐내각사", "궐내각사가 있다면, 궐외각도 있었겠지? 광화문 바깥 육조 거리(지금의 광화문 광장)에 주로 모여 있던 관청들을 궐외 각사라고 했어."),
        SolveData("집현전은 지금은 수정전(修政殿) 자리", "세종 때의 집현전은 세조때 예문관으로 이름을 바꾸고 왕의 명령을 정리하는 일을 했대. 어린 조카인 단종을 쫓아내고 왕이 된 세조가 이를 반대하던 집현전 학자들과 함께 집현전도 없애버린 거지. 이후 임진왜란 때 불탄 것을 고종 때 재건하면서 '수정전'이 된 거야."),
        SolveData("왕의 출입이 빈번", "고종임금은 처음에 이곳을 침전으로 쓰셨다는군"),
        SolveData("갑오개혁", "갑오개현은 갑오년(1894)에 이루어진 여러 가지 개혁 조치를 말해"),
    )
}