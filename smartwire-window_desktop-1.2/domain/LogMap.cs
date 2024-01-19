using System.Collections.Specialized;

namespace smartwire_window_desktop.domain
{
    internal class LogMap
    {
        private readonly OrderedDictionary logMapping = new OrderedDictionary();

        public OrderedDictionary GetLogMapping() { return logMapping; }

        public LogMap() 
        {
            logMapping.Add("작업 재시작", "start_작업 재시작");
            logMapping.Add("Nc File:", "start_작업 시작");
            logMapping.Add("가공감지", "start_가공 재시작");
            logMapping.Add("Reset", "reset_리셋");
            logMapping.Add("M코드정지", "stop_M코드 정지");
            logMapping.Add("작업 끝", "done_작업 완료");
            logMapping.Add("Wire Contact Auto Stop", "stop_와이어 접촉");
            logMapping.Add("작업중 30sec 접촉 정지", "stop_와이어 30초 접촉");
            logMapping.Add("작업중 단선", "stop_작업중 단선");
            logMapping.Add("보빈 와이어 단선", "stop_보빈 와이어 단선");
            logMapping.Add("M20-삽입실패",  "stop_자동결선 삽입실패(M20)");
            logMapping.Add("M21-절단실패", "stop_자동결선 절단실패(M21)");
            logMapping.Add("M21-잔여와이어 처리실패", "stop_자동결선 잔여와이어 처리실패(M21)");
            logMapping.Add("와이어 미동작", "stop_와이어 미동작");
            logMapping.Add("가공액 미동작", "stop_가공액 미동작");
            logMapping.Add("자동결선 FEED MOTOR ALARM", "stop_자동결선 FEED MOTOR ALARM");
            logMapping.Add("자동결선 절단 공정 실패", "stop_자동결선 절단 공정 실패");
            logMapping.Add("자동결선 잔여 WIRE 처리 실패", "stop_자동결선 잔여 WIRE 처리 실패");
            logMapping.Add("자동결선 하부 뭉치 WIRE CONTACT", "stop_자동결선 하부 뭉치 WIRE CONTACT");
            logMapping.Add("자동결선 상부 센서 WIRE CONTACT", "stop_자동결선 상부 센서 WIRE CONTACT");
            logMapping.Add("회수부 와이어 이탈", "stop_회수부 와이어 이탈");
            logMapping.Add("AWF 명령끝날때까지 센서감지", "stop_AWF 명령끝날때까지 센서감지");
            logMapping.Add("작업중 정지", "stop_작업중 정지");
            logMapping.Add("Work Tank Fluid Sensor Abnormal", "stop_오일센서 이상 감지");
            logMapping.Add("Auto Door Sensor Abnormal", "stop_자동문센서 이상 감지");
            logMapping.Add("Ready On", "stop_Ready On");
            logMapping.Add("Emergency Stop", "stop_비상정지");
            logMapping.Add("READY Off", "stop_READY Off");
            logMapping.Add("Initialization", "stop_와이어 기계 연결 완료");
            logMapping.Add("SPM Device Closed", "stop_와이어 기계 전원 종료됨");
            logMapping.Add("SPM Device DisConnected", "stop_와이어 기계 연결 끊어짐");
            logMapping.Add("SPM Device Open Succeeded", "stop_와이어 기계 전원 켜짐");
            

            // 영어 버전
            logMapping.Add("ReStart Working", "start_작업 재시작");
            logMapping.Add("Stop Working(User)", "stop_작업중 정지(사용자)");
            logMapping.Add("Mcode", "stop_M코드 정지");
            logMapping.Add("End Working", "done_작업 완료");
            logMapping.Add("30sec Contact Stop", "stop_와이어 30초 접촉");
            //logMapping.Add("작업중 단선", "stop_작업중 단선");
            logMapping.Add("Bobbin Wire breakage", "stop_보빈 와이어 단선");
            logMapping.Add("M20-Insert Error", "stop_자동결선 삽입실패(M20)");
            logMapping.Add("M21-Cutting Error", "stop_자동결선 절단실패(M21)");
            //logMapping.Add("M21-잔여와이어 처리실패", "stop_자동결선 잔여와이어 처리실패(M21)");
            //logMapping.Add("와이어 미동작", "stop_와이어 미동작");
            //logMapping.Add("가공액 미동작", "stop_가공액 미동작");
            logMapping.Add("Air Pressure abnormal", "stop_Air Pressure abnormal");
            logMapping.Add("AWF Feed Motor Stop", "stop_AWF Feed Motor Stop");
            //logMapping.Add("자동결선 절단 공정 실패", "stop_자동결선 절단 공정 실패");
            //logMapping.Add("자동결선 잔여 WIRE 처리 실패", "stop_자동결선 잔여 WIRE 처리 실패");
            //logMapping.Add("자동결선 하부 뭉치 WIRE CONTACT", "stop_자동결선 하부 뭉치 WIRE CONTACT");
            logMapping.Add("Auto Wire Feeding  Upper Sensor Wire Contact", "stop_자동결선 상부 센서 WIRE CONTACT");
            logMapping.Add("Auto Start Fail", "stop_자동 재시작 실패");
            //logMapping.Add("AWF 명령끝날때까지 센서감지", "stop_AWF 명령끝날때까지 센서감지");
            logMapping.Add("Stop Working", "stop_작업중 정지");
        }
    }
}
