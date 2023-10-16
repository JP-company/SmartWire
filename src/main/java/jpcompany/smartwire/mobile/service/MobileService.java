package jpcompany.smartwire.mobile.service;

import jpcompany.smartwire.mobile.dto.FcmTokenAndAlarmSettingDto;
import jpcompany.smartwire.mobile.repository.MobileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MobileService {

    private final MobileRepository mobileRepository;

    public void saveFcmTokenAndAlarmSettingAtDB(FcmTokenAndAlarmSettingDto fcmTokenAndAlarmSettingDto) {
        mobileRepository.saveFcmTokenAndAlarmSetting(fcmTokenAndAlarmSettingDto);
    }
}
